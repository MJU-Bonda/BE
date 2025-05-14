package bonda.bonda.domain.recentviewarticle;

import bonda.bonda.domain.article.domain.Article;
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
@Table(name = "recent_view_article")
public class RecentViewArticle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(name = "view_date")
    private LocalDateTime viewDate;

    @Builder
    public RecentViewArticle(Member member, Article article) {
        this.member = member;
        this.article = article;
        this.viewDate = LocalDateTime.now();
    }
}
