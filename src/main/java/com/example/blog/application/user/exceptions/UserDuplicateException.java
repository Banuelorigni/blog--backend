package com.example.blog.application.user.exceptions;

import com.example.blog.support.exceptions.BaseException;

public class UserDuplicateException extends BaseException {
    public UserDuplicateException(String username) {
        super("账号已存在: " + username);
    }
}
