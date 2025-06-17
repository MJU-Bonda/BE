package bonda.bonda.domain.bookarticle.repository;

import bonda.bonda.domain.article.domain.Article;
import bonda.bonda.domain.bookarticle.BookArticle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookArticleRepository extends JpaRepository<BookArticle, Long> {

    @Query("SELECT ba FROM BookArticle ba JOIN FETCH ba.article WHERE ba.book.id = :bookId ORDER BY ba.createdAt DESC")
    List<BookArticle> findWithArticleByBookId(@Param("bookId") Long bookId, Pageable pageable);

}
