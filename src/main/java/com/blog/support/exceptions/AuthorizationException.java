package com.blog.support.exceptions;



public class AuthorizationException extends HttpException {
    public AuthorizationException(ErrorCode code) {
        super(code);
    }
}
