package com.ssm.common.constant;

/**
 * 服务错误码
 */
public enum ServiceCode {

    SUCCESS(0, "请求成功"),
    SERVER_ERROR(-9999,"服务错误，攻城狮正在修复。"),
    ;

    public int code;
    public String msg;

    ServiceCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
