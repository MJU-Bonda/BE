package bonda.bonda.domain.article.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ArticleCategory {

    CATEGORY("CATEGORY", "카테고리");

    final private String key;
    final private String value;
}
