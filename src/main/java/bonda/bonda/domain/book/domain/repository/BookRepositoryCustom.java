package bonda.bonda.domain.book.domain.repository;

import bonda.bonda.domain.book.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface BookRepositoryCustom {
    Book queryDslInitTest(String name); // QueryDsl 테스트 용 메소드

    Optional<Page<Book>> findBookListByCategory(Pageable pageable, String orderBy, String category);
    Optional<Page<Book>> searchBookList(Pageable pageable, String orderBy, String word);

    List<Book> findLovedBookList(String subject);
}
