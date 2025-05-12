package bonda.bonda.domain.book.domain;

import bonda.bonda.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "book")
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private String title;

    private String writer;

    private String publisher;

    private String size;    // 판형

    @Column(name = "publish_date")
    private LocalDate publishDate;

    private Integer page;

    private String introduction;    // 도서 카드용 소개글

    private String content;         // 도서 소개 본문

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookCategory bookCategory;      // 도서 카테고리

    @Enumerated(EnumType.STRING)
    private Subject subject;        // 도서 주제

    @Builder
    public Book(String image, String title, String writer, String publisher, String size, LocalDate publishDate, Integer page, String introduction, String content, BookCategory bookCategory, Subject subject) {
        this.image = image;
        this.title = title;
        this.writer = writer;
        this.publisher = publisher;
        this.size = size;
        this.publishDate = publishDate;
        this.page = page;
        this.introduction = introduction;
        this.content = content;
        this.bookCategory = bookCategory;
        this.subject = subject;
    }
}
