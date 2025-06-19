package bonda.bonda.domain.book.domain.repository;

import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> , BookRepositoryCustom {

    List<Book> findTop3BySubjectOrderByPublishDateDesc(Subject subject);

    List<Book> findTop3ByOrderByPublishDateDesc();
}
