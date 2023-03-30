package com.example.blog.support.handlers;

import com.example.blog.application.articles.exceptions.ArticleNotFoundException;
import com.example.blog.application.comments.exceptions.CommentNotFoundException;
import com.example.blog.application.tags.exceptions.TagNotFoundException;
import com.example.blog.support.exceptions.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

}
