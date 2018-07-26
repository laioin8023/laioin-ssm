package com.ssm.service.services.impl;

import com.ssm.common.pagination.Pagination;
import com.ssm.service.entry.User;
import com.ssm.service.mapper.UserMapper;
import com.ssm.service.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据id查询用户
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public Map<String, Object> findById(Integer userId) {
        Map<String, Object> map = new TreeMap<String, Object>();
        map.put("userId", "1");
        map.put("sex", "1");
        map.put("phone", "1316171665");
        map.put("nickName", "阳光沙滩");
        return map;
    }

    /**
     * 根据 id 查询用户
     *
     * @param id 用户id
     * @return
     */
    @Override
    public User queryById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询用户
     *
     * @param pageIndex 开始页码
     * @param pageSize  每页多大
     * @return
     */
    @Override
    public Pagination<List<User>> findPage(Integer pageIndex, Integer pageSize) {
        Pagination<List<User>> pagination = new Pagination<>(pageIndex, pageSize);
        // 分页查询
        List<User> list = userMapper.selectAllUser(pagination);
        pagination.setResultList(list);
        return pagination;
    }

    /**
     * 根据性别查询用户
     *
     * @param sex       性别
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public Pagination<List<User>> findPageBySex(Integer sex, Integer pageIndex, Integer pageSize) {
        Pagination<List<User>> pagination = new Pagination<>(pageIndex, pageSize);
        // 分页查询
        List<User> list = userMapper.selectBySex(sex, pagination);
        pagination.setResultList(list);
        return pagination;
    }
}
