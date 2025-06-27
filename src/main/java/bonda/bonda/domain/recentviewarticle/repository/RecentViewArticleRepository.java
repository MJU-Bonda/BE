package bonda.bonda.domain.recentviewarticle.repository;

import bonda.bonda.domain.article.domain.Article;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.recentviewarticle.RecentViewArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecentViewArticleRepository extends JpaRepository<RecentViewArticle, Long> {

    int countByMember(Member member);

    Optional<RecentViewArticle> findByMemberAndArticle(Member member, Article article);

}
