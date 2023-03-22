package com.example.blog.application.articles;

import com.example.blog.domain.articles.Article;
import com.example.blog.domain.articles.ArticleRepository;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ArticleApplicationService {
    private ArticleRepository articleRepository;
    public Integer countWordNumber(String content) {
        return content.length();
    }

    public Article createArticles(Article article) {
        return articleRepository.save(article);
    }
    public String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
