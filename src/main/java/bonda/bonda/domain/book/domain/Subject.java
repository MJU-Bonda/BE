package bonda.bonda.domain.book.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Subject {

    SUBJECT("SUBJECT", "주제");

    final private String key;
    final private String value;
}
