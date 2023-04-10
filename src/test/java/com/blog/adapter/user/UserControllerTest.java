package com.blog.adapter.user;

import com.blog.BlogApplication;
import com.blog.adapter.user.dto.request.UserLoginRequest;
import com.blog.adapter.user.dto.request.UserRegisterRequest;
import com.blog.application.user.UserApplicationService;
import com.blog.application.user.exceptions.UserDuplicateException;
import com.blog.domain.user.RoleEnum;
import com.blog.domain.user.User;
import com.blog.infrastructure.user.entity.RoleEntity;
import com.blog.infrastructure.user.entity.UserEntity;
import com.blog.security.UserPrincipalInfo;
import com.blog.support.exceptions.ErrorCode;
import com.blog.support.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;
import java.time.Instant;
import java.util.Collections;

import static com.blog.adapter.user.dto.request.UserRegisterRequest.ERROR_MESSAGE_PASSWORD;
import static com.blog.adapter.user.dto.request.UserRegisterRequest.ERROR_MESSAGE_USERNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BlogApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerTest {
    public static final String CORRECT_USERNAME = "testUsername";
    public static final String CORRECT_PASSWORD = "P@ssword1";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JwtUtils jwtUtils;
    @MockBean
    private UserApplicationService userApplicationService;
    private UserRegisterRequest request;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().username(CORRECT_USERNAME).password(CORRECT_PASSWORD).build();
        request = UserRegisterRequest.builder().username(CORRECT_USERNAME).password(CORRECT_PASSWORD).build();
    }

    @Test
    void should_get_success_when_user_register_success() throws Exception {
        Mockito.doNothing().when(userApplicationService).register(refEq(user));

        mvc.perform(MockMvcRequestBuilders
                .post("/users/register")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isCreated());
    }

    @Test
    void should_get_bad_request_when_username_not_validation() throws Exception {
        request.setUsername("testUsername###");
        Mockito.doNothing().when(userApplicationService).register(refEq(user));

        mvc.perform(MockMvcRequestBuilders
                        .post("/users/register")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ERROR_MESSAGE_USERNAME));
    }

    @Test
    void should_get_badRequest_when_password_not_validation() throws Exception {
        request.setPassword("password");
        Mockito.doNothing().when(userApplicationService).register(refEq(user));

        mvc.perform(MockMvcRequestBuilders
                        .post("/users/register")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ERROR_MESSAGE_PASSWORD));
    }


    @Test
    void should_return_token_successfully_when_username_and_password_match() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .username(CORRECT_USERNAME)
                .password(CORRECT_PASSWORD)
                .build();
        UserPrincipalInfo userPrincipalInfo = UserPrincipalInfo.builder()
                .userEntity(new UserEntity(1L, RoleEntity.builder().id(1L).build(), CORRECT_USERNAME, CORRECT_PASSWORD, CORRECT_USERNAME,null))
                .build();
        when(userApplicationService.generatePrincipal(userLoginRequest.getUsername(), userLoginRequest.getPassword())).thenReturn(userPrincipalInfo);
        when(userApplicationService.authenticate(userPrincipalInfo, RoleEnum.OPERATOR)).thenReturn("token");

        mvc.perform(MockMvcRequestBuilders
                        .post("/users/login")
                        .header("platform", "portal")
                        .content(new ObjectMapper().writeValueAsString(userLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(CORRECT_USERNAME));
    }

    @Test
    void should_return_badRequest_when_username_is_empty() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .username("")
                .password(CORRECT_PASSWORD)
                .build();

        mvc.perform(MockMvcRequestBuilders
                        .post("/users/login")
                        .header("platform", "portal")
                        .content(new ObjectMapper().writeValueAsString(userLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("用户名不能为空"));
    }

    @Test
    void should_return_badRequest_when_password_is_empty() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .username(CORRECT_USERNAME)
                .password("")
                .build();

        mvc.perform(MockMvcRequestBuilders
                        .post("/users/login")
                        .header("platform", "portal")
                        .content(new ObjectMapper().writeValueAsString(userLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("密码不能为空"));
    }



    @Test
    @Sql("classpath:scripts/insert_an_admin_user.sql")
    void should_get_success_when_user_logout_success() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/users/logout")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie("blog_token", jwtUtils.createJwtToken(1L, "ADMIN", "libingbing")))
        ).andExpect(status().isOk());
    }

    @Test
    @Sql("classpath:scripts/insert_a_portal_user.sql")
    @Sql("classpath:scripts/insert_an_admin_user.sql")
    void should_get_user_list_with_pagination_success() throws Exception {
        User user = User
                .builder()
                .id(1L)
                .nickname("portal_user")
                .createdAt(Instant.parse("2022-10-12T13:19:53Z"))
                .build();

        Pageable pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.fromString("DESC"), "createdAt"));
        Page<User> users = new PageImpl<>(Collections.singletonList(user),pageable, 1);
        when(userApplicationService.findAll(pageable)).thenReturn(users);

        mvc.perform(MockMvcRequestBuilders
                        .get("/users?size=15")
                        .cookie(new Cookie("blog_token", jwtUtils.createJwtToken(1L, "ADMIN", "libingbing")))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.pageSize").value(15))
                .andExpect(jsonPath("$.totalPage").value(1))
                .andExpect(jsonPath("$.contents[0].id").value(1))
                .andExpect(jsonPath("$.contents[0].nickname").value("portal_user"))
                .andExpect(jsonPath("$.contents[0].createdAt").value("2022-10-12T13:19:53Z"));
    }
}
