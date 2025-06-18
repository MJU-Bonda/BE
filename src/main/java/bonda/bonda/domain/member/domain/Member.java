package bonda.bonda.domain.member.domain;

import bonda.bonda.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id", nullable = false, unique = true)
    private String kakaoId;     // 카카오 아이디

    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "save_count")
    private Integer saveCount;  // 도서 저장 수 (아티클 X)

    @Column(name = "badge_count")
    private Integer badgeCount; // 뱃지 수

    @Column(name = "auto_save")
    private Boolean autoSave;   // 검색어 자동저장 여부

    @Builder
    public Member(String kakaoId, String nickname, String profileImage) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.saveCount = 0;
        this.badgeCount = 0;
        this.autoSave = true;
    }

    public void plusSaveCount() {
        this.saveCount++;
    }

    public void minusSaveCount() {this.saveCount--;}

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void plusBadgeCount() {
        this.badgeCount++;
    }

}
