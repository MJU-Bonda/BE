package bonda.bonda.domain.article.domain;

import bonda.bonda.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "article")
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private String title;

    private String introduction;

    private String content;

    @Enumerated(EnumType.STRING)
    private ArticleCategory articleCategory;

    @Builder
    public Article(String image, String title, String introduction, String content, ArticleCategory articleCategory) {
        this.image = image;
        this.title = title;
        this.introduction = introduction;
        this.content = content;
        this.articleCategory = articleCategory;
    }
}
