package bonda.bonda.domain.memberbadge.repository;

import bonda.bonda.domain.badge.domain.Badge;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.memberbadge.MemberBadge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long> {

    boolean existsByMemberAndBadge(Member member, Badge badge);
}
