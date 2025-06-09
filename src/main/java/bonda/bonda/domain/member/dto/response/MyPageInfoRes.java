package bonda.bonda.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyPageInfoRes {

    @Schema(type = "String", example = "nickname", description = "회원의 닉네임입니다.")
    private String nickname;

    @Schema(type = "String", example = "profileImage.png", description = "회원의 프로필 이미지입니다.")
    private String profileImage;

    @Schema(type = "Integer", example = "27", description = "회원의 수집한 도서 수입니다.")
    private Integer savedBookCount;

    @Schema(type = "Integer", example = "8", description = "회원이 획득한 뱃지의 수입니다.")
    private Integer badgeCount;
}
