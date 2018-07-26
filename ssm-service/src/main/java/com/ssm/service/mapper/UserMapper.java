package com.ssm.service.mapper;

import com.ssm.common.pagination.Pagination;
import com.ssm.service.entry.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 查询所有 用户
     *
     * @param pagination 分页
     * @return
     */
    List<User> selectAllUser(Pagination<List<User>> pagination);

    /**
     * 根据性别查询用户
     *
     * @param sex        性别
     * @param pagination 分页
     * @return
     */
    List<User> selectBySex(@Param("sex") Integer sex,
                           Pagination<List<User>> pagination);
}