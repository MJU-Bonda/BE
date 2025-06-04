package bonda.bonda.domain.badge.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProgressType {

    BOOK_VIEW("BOOK_VIEW", "도서 조회수"),
    ARTICLE_VIEW("ARTICLE_VIEW", "아티클 조회수"),
    BOOK_SAVE("BOOK_SAVE", "도서 저장수"),
    ARTICLE_SAVE("ARTICLE_SAVE", "아티클 저장수");

    final private String key;
    final private String value;
}
