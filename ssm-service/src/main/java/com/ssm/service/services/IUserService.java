package com.ssm.service.services;

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
}
