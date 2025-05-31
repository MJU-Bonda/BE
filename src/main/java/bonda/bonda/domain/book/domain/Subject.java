package bonda.bonda.domain.book.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Subject {


    ALL("ALL", "전체"),
    COOKING("COOKING","요리"),
    SELF_DEVELOPMENT("SELF_DEVELOPMENT","자기계발"),
    TRAVEL("TRAVEL","로컬/여행"),
    MINIMALISM("MINIMALISM","미니멀리즘"),
    MOVIE("MOVIE","영화"),
    PET("PET","반려동물"),
    INTERIOR("INTERIOR","인테리어"),
    COFFEE("COFFEE","커피"),
    VEGAN("VEGAN","비건"),
    FITNESS("FITNESS","운동"),
    PLANT("PLANT","반려식물"),
    BURNOUT("BURNOUT","번아웃"),
    MUSIC("MUSIC","음악");

    final private String key;
    final private String value;
}
