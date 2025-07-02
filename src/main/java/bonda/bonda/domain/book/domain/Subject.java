package bonda.bonda.domain.book.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Subject {


    ALL("ALL", "전체"),
    COOKING("COOKING", "음식/요리"),
    SELF_DEVELOPMENT("SELF_DEVELOPMENT", "자기계발"),
    ART("ART", "예술"),
    SPACE("SPACE", "공간"),
    MOVIE("MOVIE", "영화"),
    PET("PET", "반려동물"),
    LOVE("LOVE", "사랑"),
    COFFEE("COFFEE", "커피"),
    COMFORT("COMFORT", "위로"),
    PLANT("PLANT", "자연/식물"),
    BURNOUT("BURNOUT", "번아웃"),
    MUSIC("MUSIC", "음악");

    final private String key;
    final private String value;

    public static boolean isValid(String subject) {
        if (subject == null) return false;
        String upper = subject.toUpperCase();
        return Arrays.stream(values())
                .anyMatch(s -> s.getKey().equalsIgnoreCase(upper));
    }
    public static Subject getByKey(String key) {
        if (key == null) return null;
        String upper = key.toUpperCase();
        return Arrays.stream(values())
                .filter(s -> s.getKey().equalsIgnoreCase(upper))
                .findFirst()
                .orElse(null);
    }
}
