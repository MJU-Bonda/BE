package bonda.bonda.domain.book.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = 976490104L;

    public static final QBook book = new QBook("book");

    public final bonda.bonda.domain.common.QBaseEntity _super = new bonda.bonda.domain.common.QBaseEntity(this);

    public final EnumPath<BookCategory> bookCategory = createEnum("bookCategory", BookCategory.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final StringPath introduction = createString("introduction");

    public final NumberPath<Integer> page = createNumber("page", Integer.class);

    public final DatePath<java.time.LocalDate> publishDate = createDate("publishDate", java.time.LocalDate.class);

    public final StringPath publisher = createString("publisher");

    public final StringPath size = createString("size");

    public final EnumPath<Subject> subject = createEnum("subject", Subject.class);

    public final StringPath title = createString("title");

    public final StringPath writer = createString("writer");

    public QBook(String variable) {
        super(Book.class, forVariable(variable));
    }

    public QBook(Path<? extends Book> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBook(PathMetadata metadata) {
        super(Book.class, metadata);
    }

}

