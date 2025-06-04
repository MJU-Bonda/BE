package bonda.bonda.domain.badge.domain.repository;

import bonda.bonda.domain.badge.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

}
