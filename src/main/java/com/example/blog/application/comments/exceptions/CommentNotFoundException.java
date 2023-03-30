package com.example.blog.application.comments.exceptions;

import com.example.blog.support.exceptions.BaseException;

public class CommentNotFoundException extends BaseException {

    public CommentNotFoundException(String message) {
        super(message+ "不存在");
    }
}
