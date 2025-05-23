package bonda.bonda.domain.book.domain.repository;

import bonda.bonda.domain.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> , BookRepositoryCustom {

    Integer countByBookCategory(String category);
}
