package bonda.bonda.domain.recentviewbook.repository;

import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.recentviewbook.RecentViewBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecentViewBookRepository extends JpaRepository<RecentViewBook, Long> {

    Optional<RecentViewBook> findByMemberAndBook(Member member, Book book);

    int countByMember(Member member);
}
