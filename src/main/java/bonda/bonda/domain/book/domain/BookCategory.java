package bonda.bonda.domain.book.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookCategory {

    CATEGORY("CATEGORY", "카테고리");

    final private String key;
    final private String value;
}
