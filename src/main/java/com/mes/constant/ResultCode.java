package com.mes.constant;

import lombok.Getter;

/**
 * 全局返回状态码枚举
 */
@Getter
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权或token失效"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    /**
     * 业务异常通用码
     */
    BUSINESS_ERROR(400, "业务操作失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 提示信息
     */
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}