package bonda.bonda.domain.articlecase;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticlecase is a Querydsl query type for Articlecase
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticlecase extends EntityPathBase<Articlecase> {

    private static final long serialVersionUID = 563219700L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticlecase articlecase = new QArticlecase("articlecase");

    public final bonda.bonda.domain.common.QBaseEntity _super = new bonda.bonda.domain.common.QBaseEntity(this);

    public final bonda.bonda.domain.article.domain.QArticle article;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final bonda.bonda.domain.member.domain.QMember member;

    public QArticlecase(String variable) {
        this(Articlecase.class, forVariable(variable), INITS);
    }

    public QArticlecase(Path<? extends Articlecase> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticlecase(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticlecase(PathMetadata metadata, PathInits inits) {
        this(Articlecase.class, metadata, inits);
    }

    public QArticlecase(Class<? extends Articlecase> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.article = inits.isInitialized("article") ? new bonda.bonda.domain.article.domain.QArticle(forProperty("article")) : null;
        this.member = inits.isInitialized("member") ? new bonda.bonda.domain.member.domain.QMember(forProperty("member")) : null;
    }

}

