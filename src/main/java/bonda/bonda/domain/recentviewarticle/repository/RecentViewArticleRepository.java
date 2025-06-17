package bonda.bonda.domain.recentviewarticle.repository;

import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.recentviewarticle.RecentViewArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentViewArticleRepository extends JpaRepository<RecentViewArticle, Long> {

    int countByMember(Member member);
}
