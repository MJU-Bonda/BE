package bonda.bonda.domain.recentviewbook;

import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.common.BaseEntity;
import bonda.bonda.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "recent_view_book")
public class RecentViewBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "view_date")
    private LocalDateTime viewDate;

    @Builder
    public RecentViewBook(Member member, Book book) {
        this.member = member;
        this.book = book;
        this.viewDate = LocalDateTime.now();
    }
}
