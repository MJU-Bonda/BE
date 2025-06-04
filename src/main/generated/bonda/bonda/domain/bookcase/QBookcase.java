package bonda.bonda.domain.bookcase;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookcase is a Querydsl query type for Bookcase
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookcase extends EntityPathBase<Bookcase> {

    private static final long serialVersionUID = 1354616740L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookcase bookcase = new QBookcase("bookcase");

    public final bonda.bonda.domain.common.QBaseEntity _super = new bonda.bonda.domain.common.QBaseEntity(this);

    public final bonda.bonda.domain.book.domain.QBook book;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final bonda.bonda.domain.member.domain.QMember member;

    public QBookcase(String variable) {
        this(Bookcase.class, forVariable(variable), INITS);
    }

    public QBookcase(Path<? extends Bookcase> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookcase(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookcase(PathMetadata metadata, PathInits inits) {
        this(Bookcase.class, metadata, inits);
    }

    public QBookcase(Class<? extends Bookcase> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new bonda.bonda.domain.book.domain.QBook(forProperty("book")) : null;
        this.member = inits.isInitialized("member") ? new bonda.bonda.domain.member.domain.QMember(forProperty("member")) : null;
    }

}

