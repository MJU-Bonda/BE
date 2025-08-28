package bonda.bonda.domain.book.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;


@AllArgsConstructor
@Getter
public enum BookCategory {

    ALL("ALL", "전체"),
    POEM("POEM", "시집"),
    NOVEL("NOVEL", "소설"),
    ESSAY("ESSAY", "에세이"),
    CARTOON("CARTOON", "만화"),
    PHOTO_BOOK("PHOTO_BOOK", "사진집"),
    ART_BOOK("ART_BOOK","아트북"),
    ILLUSTRATION("ILLUSTRATION","일러스트북"),
    MAGAZINE("MAGAZINE", "매거진");

    final private String key;
    final private String value;

    public static boolean isValid(String category) {
        if (category == null) return false;
        String upper = category.toUpperCase();
        return Arrays.stream(values())
                .anyMatch(c -> c.getKey().equalsIgnoreCase(upper));
    }
}
