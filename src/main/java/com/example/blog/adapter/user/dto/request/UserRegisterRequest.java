package com.example.blog.adapter.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRegisterRequest {

    private static final String PATTERN_USERNAME = "^[A-Za-z0-9]*$";

    private static final String PATTERN_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[-~!@#$%^&*()_+=<>?:{}|,./;'·\"\\\\\\[\\]])"
            + "[a-zA-Z0-9-~!@#$%^&*()_+=<>?:{}|,./;'·\"\\\\\\[\\]]+$";

    public static final String ERROR_MESSAGE_USERNAME = "用户名只能使用大小写字母或数字";
    public static final String ERROR_MESSAGE_PASSWORD = "密码要同时包含大小写字母、数字和特殊符号";

    @NotBlank(message = "用户名不能为空")
    @Size(min = 8, max = 16, message = "用户名长度为：8~16 位")
    @Pattern(regexp = PATTERN_USERNAME, message = ERROR_MESSAGE_USERNAME)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 16, message = "密码长度为：8~16 位")
    @Pattern(regexp = PATTERN_PASSWORD, message = ERROR_MESSAGE_PASSWORD)
    private String password;
}
