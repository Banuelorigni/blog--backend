package com.blog.application.articles;

import com.blog.adapter.articles.repository.ArticleRepository;
import com.blog.domain.articles.Article;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ArticleApplicationService {
    private ArticleRepository articleRepository;
    @ExcludeFromJacocoGeneratedReport
    public Integer countWordNumber(String content) {
        return content.length();
    }

    public Article createArticles(Article article) {
        return articleRepository.save(article);
    }
    @ExcludeFromJacocoGeneratedReport
    public String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public Page<Article> getAllArticles(String orderBy, String sortBy, int page, int size) {
        return articleRepository.getAllArticles(orderBy,sortBy,page,size);
    }

    public Article getArticleById(Long articleId) {
        return articleRepository.getArticleById(articleId);
    }
}
