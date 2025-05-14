package bonda.bonda.domain.recentviewbook;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecentViewBook is a Querydsl query type for RecentViewBook
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecentViewBook extends EntityPathBase<RecentViewBook> {

    private static final long serialVersionUID = 1416861988L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecentViewBook recentViewBook = new QRecentViewBook("recentViewBook");

    public final bonda.bonda.domain.common.QBaseEntity _super = new bonda.bonda.domain.common.QBaseEntity(this);

    public final bonda.bonda.domain.book.domain.QBook book;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final bonda.bonda.domain.member.domain.QMember member;

    public final DateTimePath<java.time.LocalDateTime> viewDate = createDateTime("viewDate", java.time.LocalDateTime.class);

    public QRecentViewBook(String variable) {
        this(RecentViewBook.class, forVariable(variable), INITS);
    }

    public QRecentViewBook(Path<? extends RecentViewBook> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecentViewBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecentViewBook(PathMetadata metadata, PathInits inits) {
        this(RecentViewBook.class, metadata, inits);
    }

    public QRecentViewBook(Class<? extends RecentViewBook> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new bonda.bonda.domain.book.domain.QBook(forProperty("book")) : null;
        this.member = inits.isInitialized("member") ? new bonda.bonda.domain.member.domain.QMember(forProperty("member")) : null;
    }

}

