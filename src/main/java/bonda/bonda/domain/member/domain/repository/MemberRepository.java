package bonda.bonda.domain.member.domain.repository;

import bonda.bonda.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
