package com.example.blog.infrastructure.articles;

import com.example.blog.infrastructure.BaseEntity;
import com.example.blog.infrastructure.tags.TagsEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "articles")
@Where(clause = BaseEntity.SKIP_DELETED_CLAUSE)
public class ArticlesEntity extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "word_numbers", nullable = false)
    private Integer wordNumbers;

    @Column(name = "cover_url", nullable = false)
    private String coverUrl;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "articles_tags",
            joinColumns = {
                    @JoinColumn(name = "article_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id", referencedColumnName = "id")
            }
    )
    private List<TagsEntity> tags;

}
