package com.example.blog.support.handlers;

import com.example.blog.support.exceptions.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.example.blog.support.exceptions.ErrorCode.ABNORMAL_TOKEN;
import static com.example.blog.support.exceptions.ErrorCode.UNKNOWN;
import static com.example.blog.support.exceptions.ErrorCode.UNKNOWN_ROLE;


@RestController
public class FilterExceptionHandler extends BasicErrorController {

    private static final String AUTHORIZATION_EXCEPTION = "AuthorizationException";
    private static final String ACCESS_DENIED_EXCEPTION = "AccessDeniedException";

    public FilterExceptionHandler(ServerProperties serverProperties) {
        super(new DefaultErrorAttributes(), serverProperties.getError());
    }

    @Override
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity error(HttpServletRequest request) {
        getErrorProperties().setIncludeException(true);
        getErrorProperties().setIncludeMessage(ErrorProperties.IncludeAttribute.ALWAYS);
        getErrorProperties().setIncludeStacktrace(ErrorProperties.IncludeAttribute.ALWAYS);
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        String exceptionMessage = (String) body.get("exception");
        if (null == exceptionMessage) {
            return new ResponseEntity<>(body, HttpStatus.valueOf((Integer) body.get("status")));
        }
        if (exceptionMessage.contains(AUTHORIZATION_EXCEPTION)) {
            return new ResponseEntity<>(new ApiError(ABNORMAL_TOKEN.getCode(), (String) body.get("message"), ABNORMAL_TOKEN.getMessage()), HttpStatus.UNAUTHORIZED);
        }
        if (exceptionMessage.contains(ACCESS_DENIED_EXCEPTION)) {
            return new ResponseEntity<>(new ApiError(UNKNOWN_ROLE.getCode(), (String) body.get("message"), UNKNOWN_ROLE.getMessage()), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new ApiError(UNKNOWN.getCode(), (String) body.get("error"), (String) body.get("message")), HttpStatus.valueOf((Integer) body.get("status")));
    }
}
