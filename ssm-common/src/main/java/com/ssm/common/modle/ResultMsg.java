package com.ssm.common.modle;

/**
 * API 接口，返回 模型
 */
public class ResultMsg<T> {

    /**
     * code
     */
    private int code;
    /**
     * code 对应的说明
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    public int getCode() {
        return code;
    }

    public ResultMsg setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResultMsg setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResultMsg setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "ResultMsg{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static void main(String args[]) {
        ResultMsg msg = new ResultMsg().setCode(1).setMessage("sss");
        System.out.println(msg);
    }
}
