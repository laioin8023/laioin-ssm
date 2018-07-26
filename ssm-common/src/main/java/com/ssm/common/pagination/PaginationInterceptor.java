package com.ssm.common.pagination;

import com.ssm.common.utils.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;


/**
 * <p>
 * 分页拦截器
 * </p>
 *
 * @author hubin
 * @Date 2016-01-23
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PaginationInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(PaginationInterceptor.class);

    /**
     * 溢出总页数，设置第一页
     */
    private boolean overflowCurrent = false;


    /**
     * Physical Pagination Interceptor for all the queries with parameter {@link org.apache.ibatis.session.RowBounds}
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) this.realTarget(invocation.getTarget());
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        // 先判断是不是SELECT操作
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }
        RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
        /* 不需要分页的场合 */
        if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();
        Connection connection = (Connection) invocation.getArgs()[0];

        if (rowBounds instanceof Pagination) {
            Pagination page = (Pagination) rowBounds;
            if (page.isSearchCount()) {
                String countSql = this.getCountOptimize(originalSql);
                this.queryTotal(countSql, mappedStatement, boundSql, page, connection);
                if (page.getTotal() <= 0) {
                    return invocation.proceed();
                }
            }
            String buildSql = this.concatOrderBy(originalSql, page);
            originalSql = this.buildMySqlPaginationSql(page, buildSql);
        } else {
            // support physical Pagination for RowBounds
            originalSql = this.buildMySqlPaginationSql(rowBounds, originalSql);
        }

        /*
         * <p> 禁用内存分页 </p> <p> 内存分页会查询所有结果出来处理（这个很吓人的），如果结果变化频繁这个数据还会不准。</p>
         */
        metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);
        metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
        metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
        return invocation.proceed();
    }

    /**
     * 查询总记录条数
     *
     * @param sql
     * @param mappedStatement
     * @param boundSql
     * @param page
     */
    protected void queryTotal(String sql, MappedStatement mappedStatement, BoundSql boundSql, Pagination page, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), boundSql);
            parameterHandler.setParameters(statement);
            int total = 0;
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    total = resultSet.getInt(1);
                }
            }
            page.setTotal(total);
            /*
             * 溢出总页数，设置第一页
             */
            int pages = page.getPages();
            if (overflowCurrent && (page.getPageIndex() > pages)) {
                page = new Pagination(1, page.getPageSize());
                page.setTotal(total);
            }
        } catch (Exception e) {
            logger.error("Error: Method queryTotal execution error !", e);
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }


    public void setOverflowCurrent(boolean overflowCurrent) {
        this.overflowCurrent = overflowCurrent;
    }


    /**
     * 获得真正的处理对象,可能多层代理.
     */
    private Object realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        }
        return target;
    }

    /**
     * 获取 sql
     *
     * @param originalSql 需要计算Count SQL
     * @return CountOptimize
     */
    private String getCountOptimize(String originalSql) {
        // 调整SQL便于解析
        String tempSql = originalSql.replaceAll("(?i)ORDER[\\s]+BY", "ORDER BY").replaceAll("(?i)GROUP[\\s]+BY", "GROUP BY");
        String indexOfSql = tempSql.toUpperCase();
        // 有排序情况
        int orderByIndex = indexOfSql.lastIndexOf("ORDER BY");

        StringBuilder countSql = new StringBuilder("SELECT COUNT(1) ");
        boolean optimize = false;
        if (!indexOfSql.contains("DISTINCT") && !indexOfSql.contains("GROUP BY")) {
            int formIndex = indexOfSql.indexOf("FROM");
            if (formIndex > -1) {
                if (orderByIndex > -1) {
                    tempSql = tempSql.substring(0, orderByIndex);
                    countSql.append(tempSql.substring(formIndex));
                    // 无排序情况
                } else {
                    countSql.append(tempSql.substring(formIndex));
                }
                // 执行优化
                optimize = true;
            }
        }
        if (!optimize) {
            // 无优化SQL
            countSql.append("FROM ( ").append(originalSql).append(" ) TOTAL");
        }
        return countSql.toString();
    }

    /**
     * 查询SQL拼接Order By
     *
     * @param originalSql 需要拼接的SQL
     * @param page        page对象
     * @return
     */
    private String concatOrderBy(String originalSql, Pagination page) {
        if (StringUtils.isNotEmpty(page.getOrderByField()) && page.isOpenSort()) {
            StringBuilder buildSql = new StringBuilder(originalSql);
            buildSql.append(" ORDER BY ").append(page.getOrderByField());
            buildSql.append(page.isAsc() ? " ASC " : " DESC ");
            return buildSql.toString();
        }
        return originalSql;
    }


    /**
     * <p>
     * 生成 mysql   翻页执行 SQL
     * </p>
     *
     * @param page     翻页对象
     * @param buildSql 执行 SQL
     * @return
     * @throws Exception
     */
    private String buildMySqlPaginationSql(Pagination page, String buildSql) {
        StringBuilder sql = new StringBuilder(buildSql);
        sql.append(" LIMIT ").append(page.getOffsetCurrent()).append(",").append(page.getPageSize());
        return sql.toString();
    }

    /**
     * <p>
     * 生成 mysql   翻页执行 SQL
     * </p>
     *
     * @param rowBounds 翻页对象
     * @param buildSql  执行 SQL
     * @return
     * @throws Exception
     */
    private String buildMySqlPaginationSql(RowBounds rowBounds, String buildSql) {
        StringBuilder sql = new StringBuilder(buildSql);
        sql.append(" LIMIT ").append(rowBounds.getOffset()).append(",").append(rowBounds.getLimit());
        return sql.toString();
    }

}
