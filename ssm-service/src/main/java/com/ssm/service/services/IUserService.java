package com.ssm.service.services;

import com.ssm.service.entry.User;

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
}
