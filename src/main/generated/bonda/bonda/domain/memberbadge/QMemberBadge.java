package bonda.bonda.domain.memberbadge;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberBadge is a Querydsl query type for MemberBadge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberBadge extends EntityPathBase<MemberBadge> {

    private static final long serialVersionUID = 844925082L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberBadge memberBadge = new QMemberBadge("memberBadge");

    public final bonda.bonda.domain.common.QBaseEntity _super = new bonda.bonda.domain.common.QBaseEntity(this);

    public final bonda.bonda.domain.badge.domain.QBadge badge;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final bonda.bonda.domain.member.domain.QMember member;

    public QMemberBadge(String variable) {
        this(MemberBadge.class, forVariable(variable), INITS);
    }

    public QMemberBadge(Path<? extends MemberBadge> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberBadge(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberBadge(PathMetadata metadata, PathInits inits) {
        this(MemberBadge.class, metadata, inits);
    }

    public QMemberBadge(Class<? extends MemberBadge> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.badge = inits.isInitialized("badge") ? new bonda.bonda.domain.badge.domain.QBadge(forProperty("badge")) : null;
        this.member = inits.isInitialized("member") ? new bonda.bonda.domain.member.domain.QMember(forProperty("member")) : null;
    }

}

