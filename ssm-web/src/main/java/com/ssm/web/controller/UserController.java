package com.ssm.web.controller;

import com.ssm.common.modle.ResultMsg;
import com.ssm.common.pagination.Pagination;
import com.ssm.service.entry.User;
import com.ssm.service.services.IUserService;
import com.ssm.web.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 表示 restful controller
 *
 * @RestController
 */

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private Logger LGR = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    /**
     * 普通接口形式，获取数据
     *
     * @param userId
     * @return
     */
    @RequestMapping("/findById")
    public ResultMsg findById(Integer userId) {
        User data = userService.queryById(userId);
        LGR.info("11111");
        LGR.debug("debug");
        LGR.error("ERROR");
        LGR.error("中午呢");
        return buildSuccess(data);
    }

    /**
     * 分页查询用户
     *
     * @param pageIndex 起始页码
     * @param pageSize  页码大小
     * @return
     */
    @RequestMapping("/query/list/{pageIndex}/{pageSize}")
    public ResultMsg queryList(@PathVariable("pageIndex") Integer pageIndex,
                               @PathVariable("pageSize") Integer pageSize) {
        Pagination<List<User>> pages = userService.findPage(pageIndex, pageSize);
        return buildSuccess(pages);
    }

    /**
     * 分页查询用户,根据性别
     *
     * @param pageIndex 起始页码
     * @param pageSize  页码大小
     * @return
     */
    @RequestMapping("/query/list/{sex}/{pageIndex}/{pageSize}")
    public ResultMsg queryList(@PathVariable("sex") Integer sex,
                               @PathVariable("pageIndex") Integer pageIndex,
                               @PathVariable("pageSize") Integer pageSize) {
        Pagination<List<User>> pages = userService.findPageBySex(sex, pageIndex, pageSize);
        return buildSuccess(pages);
    }

}
