package bonda.bonda.domain.memberbadge.repository;

import bonda.bonda.domain.badge.domain.Badge;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.memberbadge.MemberBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long> {

    boolean existsByMemberAndBadge(Member member, Badge badge);

    List<MemberBadge> findByMember(Member member);

    Optional<MemberBadge> findByMemberAndBadge(Member member, Badge badge);
}
