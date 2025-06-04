package bonda.bonda.domain.bookarticle;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookArticle is a Querydsl query type for BookArticle
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookArticle extends EntityPathBase<BookArticle> {

    private static final long serialVersionUID = -1719055390L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookArticle bookArticle = new QBookArticle("bookArticle");

    public final bonda.bonda.domain.common.QBaseEntity _super = new bonda.bonda.domain.common.QBaseEntity(this);

    public final bonda.bonda.domain.article.domain.QArticle article;

    public final bonda.bonda.domain.book.domain.QBook book;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QBookArticle(String variable) {
        this(BookArticle.class, forVariable(variable), INITS);
    }

    public QBookArticle(Path<? extends BookArticle> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookArticle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookArticle(PathMetadata metadata, PathInits inits) {
        this(BookArticle.class, metadata, inits);
    }

    public QBookArticle(Class<? extends BookArticle> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.article = inits.isInitialized("article") ? new bonda.bonda.domain.article.domain.QArticle(forProperty("article")) : null;
        this.book = inits.isInitialized("book") ? new bonda.bonda.domain.book.domain.QBook(forProperty("book")) : null;
    }

}

