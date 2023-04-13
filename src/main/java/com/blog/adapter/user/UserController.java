package com.blog.adapter.user;

import com.blog.adapter.user.dto.request.UserLoginRequest;
import com.blog.adapter.user.dto.request.UserRegisterRequest;
import com.blog.adapter.user.dto.response.UserPageResponse;
import com.blog.adapter.user.dto.response.UserResponse;
import com.blog.adapter.user.mapper.UserDtoMapper;
import com.blog.application.user.UserApplicationService;
import com.blog.domain.user.RoleEnum;
import com.blog.domain.user.User;
import com.blog.security.UserPrincipalInfo;
import com.blog.support.constants.SecurityConstants;
import com.blog.support.exceptions.AuthorizationException;
import com.blog.support.exceptions.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Tag(name = "User Management API")
public class UserController {
    private final UserApplicationService userApplicationService;

    private static final String SUCCESS = "注册成功";
    private static final String LOGIN_OUT = "注销登录成功";
    private static final String PLATFORM_TYPE = "platform";

    @PostMapping("/register")
    @Operation(summary = "New user registration")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "registered the new user",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                            }),
            }
    )
    public ResponseEntity<String> register(@Parameter(description = "info of registering user") @RequestBody @Valid UserRegisterRequest request) {
        User user = UserDtoMapper.MAPPER.toModel(request);
        userApplicationService.register(user);
        return new ResponseEntity<>(SUCCESS, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "User login",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class))
                            }),
            }
    )
    public ResponseEntity<UserResponse> login(@Parameter(description = "info of logging user") @RequestBody @Valid UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String platform = request.getHeader(PLATFORM_TYPE);
        if (null == platform) {
            throw new AuthorizationException(ErrorCode.UNKNOWN_PLATFORM);
        }

        UserPrincipalInfo user = userApplicationService.generatePrincipal(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        String token = userApplicationService.authenticate(user, RoleEnum.getByPlatformName(platform));

        return ResponseEntity
                .ok()
                .header(SecurityConstants.COOKIE_NAME, SecurityConstants.getCookieValue(token))
                .body(UserDtoMapper.MAPPER.toResponse(user.getUserEntity()));
    }

    @PostMapping("/logout")
    @Operation(summary = "log out")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "log out",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                            }),
            }
    )
    public ResponseEntity<String> logout() {
        return ResponseEntity
                .ok()
                .header(SecurityConstants.COOKIE_NAME, SecurityConstants.getCookieValue(null))
                .body(LOGIN_OUT);
    }

    @GetMapping
    @Operation(summary = "Get user list")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserPageResponse fetch(
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String orderBy,
            @PageableDefault(page = 1, size = 15)
            Pageable pageable
    ) {
        Pageable pageValue = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(),
                Sort.by(Sort.Direction.fromString(orderBy), sortBy));
        return new UserPageResponse(userApplicationService.findAll(pageValue).map(UserDtoMapper.MAPPER::toUserInfoResponse));
    }
}
