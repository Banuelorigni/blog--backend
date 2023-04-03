package com.blog.application.tags.exceptions;

import com.blog.support.exceptions.BaseException;

public class TagNotFoundException extends BaseException {
    public TagNotFoundException(String message) {
        super(message + "不存在");
    }
}
