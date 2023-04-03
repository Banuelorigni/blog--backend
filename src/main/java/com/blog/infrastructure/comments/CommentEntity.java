package com.blog.infrastructure.comments;

import com.blog.infrastructure.BaseEntity;
import com.blog.infrastructure.articles.ArticlesEntity;
import com.blog.infrastructure.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
@Where(clause = BaseEntity.SKIP_DELETED_CLAUSE)
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne(targetEntity = ArticlesEntity.class)
    @JoinColumn(name = "article_id")
    private ArticlesEntity article;
}
