package com.blog.application.articles.exceptions;

import com.blog.support.exceptions.BaseException;

public class ArticleNotFoundException extends BaseException {
    public ArticleNotFoundException(String message) {
        super(message + "不存在");
    }
}
