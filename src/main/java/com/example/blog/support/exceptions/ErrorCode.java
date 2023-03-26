package com.example.blog.support.exceptions;

public enum ErrorCode {
    NO_PERMISSION(10004, "当前用户没有权限！"),
    LOGIN_FAILED(31012, "用户名或者密码错误！"),
    VERIFY_CODE_ERROR(31013, "验证码错误！"),
    ACCOUNT_ANOMALY(31014, "无效的用户信息！"),
    ABNORMAL_TOKEN(31015, "token 异常！"),
    ACCOUNT_DISABLED(31016, "账号不可用！"),
    NOT_EXIST(31018, "不存在该用户！"),
    UNKNOWN(31019, "未知异常！"),
    UNKNOWN_ROLE(31020, "该用户没有权限！"),
    ROLE_EXPIRED(31021, "该角色已经过期！"),
    DELETE_FAILED(31022, "删除失败！"),
    UNKNOWN_PLATFORM(31023, "未知的平台！"),
    VALIDATION_ERROR(10101, "校验错误！");

    private final int code;

    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
