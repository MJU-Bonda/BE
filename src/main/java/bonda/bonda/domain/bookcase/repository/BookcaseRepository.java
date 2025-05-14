package bonda.bonda.domain.bookcase.repository;

import bonda.bonda.domain.bookcase.Bookcase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookcaseRepository extends JpaRepository<Bookcase, Long> {

}
