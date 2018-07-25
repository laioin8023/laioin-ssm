package com.ssm.web.controller;

import com.ssm.common.modle.ResultMsg;
import com.ssm.service.entry.User;
import com.ssm.service.services.IUserService;
import com.ssm.web.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
