package bonda.bonda.domain.book.domain.repository;

import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.domain.BookCategory;
import bonda.bonda.domain.book.dto.response.BookDetailRes;
import bonda.bonda.domain.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface BookRepositoryCustom {
    Book queryDslInitTest(String name); // QueryDsl 테스트 용 메소드

    Optional<Page<Book>> findBookListByCategory(Pageable pageable, String orderBy, String category);
    Optional<Page<Book>> searchBookList(Pageable pageable, String orderBy, String word);

    List<Book> findLovedBookList(String subject);

    Page<Book> findMySavedBookList(Pageable pageable, String orderBy, Member member);

    BookDetailRes findBookDetailResWithIsBookMarked(Long bookId, Member member);

    Page<Book> findRecentViewBookList(Member member, Pageable pageable);

    List<Book> findRecentBookListBySubject(String subject);

    Map<BookCategory, Long> countBookcaseByCategory(Member member);
}
