package com.example.blog.adapter.comments;

import com.example.blog.adapter.comments.dto.request.CreateCommentRequest;
import com.example.blog.application.comments.CommentApplicationService;
import com.example.blog.application.user.UserApplicationService;
import com.example.blog.domain.comments.Comment;
import com.example.blog.support.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comments")
@Validated
@AllArgsConstructor
@Transactional
public class CommentController {
    private final JwtUtils jwtUtils;
    private final CommentApplicationService commentApplicationService;
    private final UserApplicationService userApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(@RequestBody @Valid CreateCommentRequest commentRequest, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Long userId = Long.valueOf(jwtUtils.getUserIdFromCookies(cookies));
        String userName = userApplicationService.findUserNameByUserId(userId);

        Comment comment = CommentDtoMapper.MAPPER.toModel(commentRequest, userName);
        return commentApplicationService.save(comment);
    }

    @GetMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Comment> getCommentByArticleId(@PathVariable Long articleId,
                                               @RequestParam(required = false, defaultValue = "DESC") String orderBy,
                                               @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
                                               @RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        return commentApplicationService.getCommentByArticleId(articleId, orderBy, sortBy, page, size);
    }
}
