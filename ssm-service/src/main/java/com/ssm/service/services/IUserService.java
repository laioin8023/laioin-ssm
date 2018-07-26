package com.ssm.service.services;

import com.ssm.common.pagination.Pagination;
import com.ssm.service.entry.User;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 */
public interface IUserService {

    /**
     * 根据id查询用户
     *
     * @param userId 用户id
     * @return
     */
    Map<String, Object> findById(Integer userId);

    /**
     * 根据 id 查询用户
     *
     * @param id 用户id
     * @return
     */
    User queryById(Integer id);

    /**
     * 分页查询用户
     *
     * @param pageIndex 开始页码
     * @param pageSize  每页多大
     * @return
     */
    Pagination<List<User>> findPage(Integer pageIndex, Integer pageSize);

    /**
     * 根据性别查询用户
     *
     * @param sex       性别
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pagination<List<User>> findPageBySex(Integer sex, Integer pageIndex, Integer pageSize);
}
