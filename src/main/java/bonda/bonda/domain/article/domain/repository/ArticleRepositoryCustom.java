package bonda.bonda.domain.article.domain.repository;

import bonda.bonda.domain.article.domain.Article;
import bonda.bonda.domain.article.dto.response.SimpleArticleResWithBookmarked;
import bonda.bonda.domain.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {
    Page<SimpleArticleResWithBookmarked> findArticleListByCategory(Pageable pageable, String category, Member member);

    Page<Article> searchArticleList(Pageable pageable, String orderBy, String word);

    Page<Article> findMySavedArticleList(Pageable pageable, String orderBy, Member member);

    Page<Article> findRecentViewArticleList(Member member, Pageable pageable);
}
