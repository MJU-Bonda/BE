package bonda.bonda.domain.articlecase.repository;

import bonda.bonda.domain.article.domain.Article;
import bonda.bonda.domain.articlecase.Articlecase;
import bonda.bonda.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticlecaseRepository extends JpaRepository<Articlecase, Long> {

    int countByMember(Member member);

    boolean existsByMemberAndArticle(Member persistMember, Article article);
}
