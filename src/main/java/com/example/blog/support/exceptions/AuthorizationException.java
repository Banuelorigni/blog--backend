package com.example.blog.support.exceptions;



public class AuthorizationException extends HttpException {
    public AuthorizationException(ErrorCode code) {
        super(code);
    }
}
