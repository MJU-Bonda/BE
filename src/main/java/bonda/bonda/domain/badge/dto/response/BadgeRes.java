package bonda.bonda.domain.badge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BadgeRes {

    @Schema(type = "Long", example = "1", description = "뱃지의 아이디 번호를 출력합니다. 상세 조회 시 사용됩니다.")
    private Long Id;

    @Schema(type = "String", example = "느긋한 발걸음", description = "뱃지의 이름을 출력합니다.")
    private String name;

    @Schema(type = "String", example = "BOOK_VIEW_1.png", description = "뱃지의 이미지 url을 출력합니다.")
    private String image;

    @Schema(type = "Boolean", example = "true", description = "뱃지 획득 여부를 출력합니다. true면 회원이 뱃지를 획득했음을 의미합니다.")
    private Boolean isUnlocked;
}
