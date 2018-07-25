package com.ssm.web.base;

import com.ssm.common.constant.ServiceCode;
import com.ssm.common.modle.ResultMsg;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {

    /**
     * 处理 服务内的错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResultMsg handlerEx(Exception e) {
        ResultMsg msg = new ResultMsg().setCode(ServiceCode.SERVER_ERROR.code).
                setMessage(ServiceCode.SERVER_ERROR.msg).setData(new Object());
        return msg;
    }

    /**
     * 构建成功 模型，返回数据
     *
     * @param data 返回的数据
     * @param <T>
     * @return
     */
    protected <T> ResultMsg<T> buildSuccess(T data) {
        ResultMsg msg = new ResultMsg().setCode(ServiceCode.SUCCESS.code).
                setMessage(ServiceCode.SUCCESS.msg).setData(data);
        return msg;
    }

    /**
     * 构建 错误模型，并且返回
     *
     * @param serviceCode 错误描述
     * @return
     */
    protected ResultMsg buildError(ServiceCode serviceCode) {
        ResultMsg msg = new ResultMsg().setCode(serviceCode.code).
                setMessage(serviceCode.msg).setData(new Object());
        return msg;
    }
}
