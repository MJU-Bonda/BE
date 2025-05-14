package bonda.bonda.domain.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1306525222L;

    public static final QMember member = new QMember("member1");

    public final bonda.bonda.domain.common.QBaseEntity _super = new bonda.bonda.domain.common.QBaseEntity(this);

    public final BooleanPath autoSave = createBoolean("autoSave");

    public final NumberPath<Integer> badgeCount = createNumber("badgeCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath kakaoId = createString("kakaoId");

    public final StringPath nickname = createString("nickname");

    public final StringPath profileImage = createString("profileImage");

    public final NumberPath<Integer> saveCount = createNumber("saveCount", Integer.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

