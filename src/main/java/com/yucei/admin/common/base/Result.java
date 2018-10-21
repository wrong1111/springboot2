package com.yucei.admin.common.base;

import org.apache.commons.lang3.StringUtils;

/**
 * @项目名称：yucei-admin
 * @类名称：Result
 * @类描述：http请求返回的对象
 * @创建人：yucei
 * @创建时间：2018年6月5日 下午4:40:52
 * @version：
 */
public class Result<T> {

    private String code = "200";// 响应状态编码
    private String message;// 响应信息
    private T data;// 返回成功信息
    private String status = code;

    public Result() {
        // 单例
    }

    private static final Result ME = new Result();


    public static Result getInstance() {
        return ME;
    }

    public Result ok(T data) {
        return ME.setCode("200").setData(data).setMessage(StringUtils.isBlank(message) ? "成功" : message);
    }

    public Result fail() {
        return ME.setCode("400").setData(null).setMessage("失败");
    }

    public Result fail(String message) {
        return ME.setCode("400").setData(null).setMessage(StringUtils.isBlank(message) ? "失败" : message);
    }

    /**
     * 响应status和message
     *
     * @param status
     * @param message
     */
    public Result(String status, String message) {
        this.code = status;
        this.status = status;
        this.message = message;
    }

    public Result(String message) {
        this.message = message;
    }

    public String getStatus() {
        return code;
    }

    /**
     * 响应status、message和result
     *
     * @param status
     * @param message
     * @param data
     */
    public Result(String status, String message, T data) {
        this.code = status;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Result setCode(String status) {
        this.code = status;
        this.status = status;
        return this;
    }

    public Result setStatus(String status) {
        this.code = status;
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "Result [code=" + code + ", message=" + message + ", data="
                + data + "]";
    }

}