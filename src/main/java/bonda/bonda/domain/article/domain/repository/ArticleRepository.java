package bonda.bonda.domain.article.domain.repository;

import bonda.bonda.domain.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
