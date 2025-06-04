package bonda.bonda.domain.article.domain.repository;

import bonda.bonda.domain.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtcleRepository extends JpaRepository<Article, Long> {

}
