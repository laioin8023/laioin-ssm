package com.ssm.common.pagination;

import com.ssm.common.utils.StringUtils;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

/**
 * 分页类
 */
public class Pagination<T> extends RowBounds implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总数
     */
    private int total;

    /**
     * 每页显示条数，默认 10
     */
    private int pageSize = 10;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 当前页
     */
    private int pageIndex = 1;

    /**
     * 查询总记录数（默认 true）
     */
    private boolean searchCount = true;

    /**
     * 开启排序（默认 true） 只在代码逻辑判断 并不截取sql分析
     **/
    private boolean openSort = true;

    /**
     * <p>
     * SQL 排序 ORDER BY 字段，例如： id DESC（根据id倒序查询）
     * </p>
     * <p>
     * DESC 表示按倒序排序(即：从大到小排序)<br>
     * ASC 表示按正序排序(即：从小到大排序)
     * </p>
     */
    private String orderByField;

    /**
     * 是否为升序 ASC（ 默认： true ）
     */
    private boolean isAsc = true;

    /**
     * 查询出来的分页数据
     */
    private T resultList;

    public Pagination() {
        super();
    }

    /**
     * <p>
     * 分页构造函数
     * </p>
     *
     * @param pageIndex 当前页
     * @param pageSize  每页显示条数
     */
    public Pagination(int pageIndex, int pageSize) {
        this(pageIndex, pageSize, true);
    }

    public Pagination(int pageIndex, int pageSize, boolean searchCount) {
        this(pageIndex, pageSize, searchCount, true);
    }

    public Pagination(int pageIndex, int pageSize, boolean searchCount, boolean openSort) {
        super(offsetCurrent(pageIndex, pageSize), pageSize);
        if (pageIndex > 1) {
            this.pageIndex = pageIndex;
        }
        this.pageSize = pageSize;
        this.searchCount = searchCount;
        this.openSort = openSort;
    }

    protected static int offsetCurrent(int current, int size) {
        if (current > 0) {
            return (current - 1) * size;
        }
        return 0;
    }

    public int getOffsetCurrent() {
        return offsetCurrent(this.pageIndex, this.pageSize);
    }

    public boolean hasPrevious() {
        return this.pageIndex > 1;
    }

    public boolean hasNext() {
        return this.pageIndex < this.pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    public int getPages() {
        if (this.pageSize == 0) {
            return 0;
        }
        this.pages = this.total / this.pageSize;
        if (this.total % this.pageSize != 0) {
            this.pages++;
        }
        return this.pages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }


    public String getOrderByField() {
        return orderByField;
    }

    public void setOrderByField(String orderByField) {
        if (StringUtils.isNotEmpty(orderByField)) {
            this.orderByField = orderByField;
        }
    }

    public boolean isOpenSort() {
        return openSort;
    }

    public void setOpenSort(boolean openSort) {
        this.openSort = openSort;
    }

    public boolean isAsc() {
        return isAsc;
    }

    public void setAsc(boolean isAsc) {
        this.isAsc = isAsc;
    }

    public T getResultList() {
        return resultList;
    }

    public void setResultList(T resultList) {
        this.resultList = resultList;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "total=" + total +
                ", pageSize=" + pageSize +
                ", pages=" + pages +
                ", pageIndex=" + pageIndex +
                ", orderByField='" + orderByField + '\'' +
                ", isAsc=" + isAsc +
                ", resultList=" + resultList +
                '}';
    }
}
