package com.ssm.service.services.impl;

import com.ssm.service.services.IUserService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
public class UserServiceImpl implements IUserService {

    /**
     * 根据id查询用户
     *
     * @param userId 用户id
     * @return
     */
    public Map<String, Object> findById(Integer userId) {
        Map<String, Object> map = new TreeMap<String, Object>();
        map.put("userId", "1");
        map.put("sex", "1");
        map.put("phone", "1316171665");
        map.put("nickName", "阳光沙滩");
        return map;
    }
}
