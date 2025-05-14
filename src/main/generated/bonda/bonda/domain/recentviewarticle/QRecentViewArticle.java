package bonda.bonda.domain.recentviewarticle;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecentViewArticle is a Querydsl query type for RecentViewArticle
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecentViewArticle extends EntityPathBase<RecentViewArticle> {

    private static final long serialVersionUID = 1286999636L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecentViewArticle recentViewArticle = new QRecentViewArticle("recentViewArticle");

    public final bonda.bonda.domain.common.QBaseEntity _super = new bonda.bonda.domain.common.QBaseEntity(this);

    public final bonda.bonda.domain.article.domain.QArticle article;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final bonda.bonda.domain.member.domain.QMember member;

    public final DateTimePath<java.time.LocalDateTime> viewDate = createDateTime("viewDate", java.time.LocalDateTime.class);

    public QRecentViewArticle(String variable) {
        this(RecentViewArticle.class, forVariable(variable), INITS);
    }

    public QRecentViewArticle(Path<? extends RecentViewArticle> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecentViewArticle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecentViewArticle(PathMetadata metadata, PathInits inits) {
        this(RecentViewArticle.class, metadata, inits);
    }

    public QRecentViewArticle(Class<? extends RecentViewArticle> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.article = inits.isInitialized("article") ? new bonda.bonda.domain.article.domain.QArticle(forProperty("article")) : null;
        this.member = inits.isInitialized("member") ? new bonda.bonda.domain.member.domain.QMember(forProperty("member")) : null;
    }

}

