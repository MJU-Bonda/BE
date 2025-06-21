package bonda.bonda.domain.article.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ArticleCategory {

    ALL("ALL", "전체"),
    AUTHOR_OR_PUBLISHER("AUTHOR_OR_PUBLISHER","작가/출판사"),
    BOOKSTORE("BOOKSTORE","독립서점"),
    THEME("THEME","테마");

    final private String key;
    final private String value;


    public static boolean isValid(String key) {
        for (ArticleCategory category : ArticleCategory.values()) {
            if (category.getKey().equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }
}
