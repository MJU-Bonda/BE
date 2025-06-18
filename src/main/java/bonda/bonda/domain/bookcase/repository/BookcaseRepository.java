package bonda.bonda.domain.bookcase.repository;

import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.bookcase.Bookcase;
import bonda.bonda.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookcaseRepository extends JpaRepository<Bookcase, Long> {

    boolean existsByMemberAndBook(Member member, Book book);

    Optional<Bookcase> findByMemberAndBook(Member member, Book book);

    int countByMember(Member member);
}
