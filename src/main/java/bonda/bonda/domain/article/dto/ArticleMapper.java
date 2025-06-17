package bonda.bonda.domain.article.dto;

import bonda.bonda.domain.article.domain.Article;
import bonda.bonda.domain.article.dto.response.SimpleArticleRes;

import java.util.List;
import java.util.stream.Collectors;

public class ArticleMapper {
    public static List<SimpleArticleRes> convertToSimpleArticleListRes(List<Article> articleList) {
        return articleList.stream()
                .map(article -> SimpleArticleRes.builder()
                        .articleId(article.getId())
                        .title(article.getTitle())
                        .articleCategory(article.getArticleCategory().getKey())
                        .imageUrl(article.getImage())
                        .build())
                .collect(Collectors.toList());
    }
}
