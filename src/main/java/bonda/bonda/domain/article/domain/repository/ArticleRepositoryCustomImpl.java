package bonda.bonda.domain.article.domain.repository;

import bonda.bonda.domain.article.domain.ArticleCategory;
import bonda.bonda.domain.article.domain.QArticle;
import bonda.bonda.domain.article.dto.response.SimpleArticleResWithBookmarked;
import bonda.bonda.domain.articlecase.QArticlecase;
import bonda.bonda.domain.member.domain.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QArticle article = QArticle.article;
    private final QArticlecase articlecase = QArticlecase.articlecase;

    @Override
    public Page<SimpleArticleResWithBookmarked> findArticleListByCategory(Pageable pageable, String category, Member loginMember) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (!"ALL".equalsIgnoreCase(category)) { //카테고리 조건 -> ALL 이면 무시
            predicate.and(article.articleCategory.eq(ArticleCategory.valueOf(category.toUpperCase())));
        }

        List<SimpleArticleResWithBookmarked> articles = jpaQueryFactory
                .select(Projections.constructor( //응답 dto로 조회
                        SimpleArticleResWithBookmarked.class,
                        article.id,
                        article.articleCategory.stringValue(),
                        article.title,
                        article.introduction,
                        article.image,
                        JPAExpressions.selectOne()
                                .from(articlecase)
                                .where(articlecase.member.eq(loginMember).and(articlecase.article.eq(article)))
                                .exists()
                ))
                .from(article)
                .where(predicate)
                .orderBy(article.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(article.count())
                .from(article)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(articles, pageable, total != null ? total : 0);
    }


}
