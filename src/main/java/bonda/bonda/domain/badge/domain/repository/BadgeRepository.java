package bonda.bonda.domain.badge.domain.repository;

import bonda.bonda.domain.badge.domain.Badge;
import bonda.bonda.domain.badge.domain.ProgressType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    List<Badge> findByProgressType(ProgressType progressType);
}
