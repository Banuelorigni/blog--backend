package com.blog.support.handlers;

import com.blog.application.articles.exceptions.ArticleNotFoundException;
import com.blog.application.comments.exceptions.CommentNotFoundException;
import com.blog.application.tags.exceptions.TagNotFoundException;
import com.blog.application.user.exceptions.UserDuplicateException;
import com.blog.support.exceptions.ApiError;
import com.blog.support.exceptions.AuthorizationException;
import com.blog.support.exceptions.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class
RestExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(MethodArgumentNotValidException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String message = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return new ApiError(
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleTagNotExist(TagNotFoundException exception) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;

        return new ApiError(badRequest.value(), badRequest.getReasonPhrase(), exception.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleArticleNotExist(ArticleNotFoundException exception) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;

        return new ApiError(badRequest.value(), badRequest.getReasonPhrase(), exception.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCommentNotExist(CommentNotFoundException exception){
        HttpStatus badRequest = HttpStatus.NOT_FOUND;

        return new ApiError(badRequest.value(), badRequest.getReasonPhrase(), exception.getMessage());
    }

    @ExceptionHandler(value = UserDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(UserDuplicateException ex) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        return new ApiError(
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                ex.getMessage()
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = AuthorizationException.class)
    public ApiError authorizationExceptionHandler(AuthorizationException ex) {
        return new ApiError(
                ex.getErrorCode().getCode(),
                ex.getErrorCode().getMessage(),
                ex.getMessage()
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = BadCredentialsException.class)
    public ApiError badCredentialsExceptionHandler(BadCredentialsException ex) {
        return new ApiError(
                ErrorCode.LOGIN_FAILED.getCode(),
                ErrorCode.LOGIN_FAILED.getMessage(),
                ex.getMessage());
    }

}
