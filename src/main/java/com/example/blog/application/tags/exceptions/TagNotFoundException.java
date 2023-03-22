package com.example.blog.application.tags.exceptions;

import com.example.blog.support.exceptions.BaseException;

public class TagNotFoundException extends BaseException {
    public TagNotFoundException(String message) {
        super(message + "不存在");
    }
}
