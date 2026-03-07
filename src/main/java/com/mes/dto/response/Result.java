package com.mes.dto.response;

import com.mes.constant.ResultCode;
import lombok.Data;

/**
 * 全局统一返回结果
 * @param <T> 数据泛型
 */
@Data
public class Result<T> {

    /**
     * 状态码
     */
    private int code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 私有化构造方法
     */
    private Result() {}

    /**
     * 成功返回（无数据）
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功返回（带数据）
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    /**
     * 失败返回（自定义状态码和信息）
     */
    public static <T> Result<T> fail(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    /**
     * 失败返回（使用枚举状态码）
     */
    public static <T> Result<T> fail(ResultCode resultCode) {
        return fail(resultCode, null);
    }

    /**
     * 失败返回（使用枚举状态码 + 自定义信息）
     */
    public static <T> Result<T> fail(ResultCode resultCode, String message) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        // 如果传入了自定义message则使用，否则使用枚举默认message
        result.setMessage(message != null && !message.isEmpty() ? message : resultCode.getMessage());
        result.setData(null);
        return result;
    }
}