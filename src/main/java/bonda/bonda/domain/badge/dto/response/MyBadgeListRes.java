package bonda.bonda.domain.badge.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MyBadgeListRes {

    @Schema(type = "Integer", example = "1", description = "회원이 갖고 있는 뱃지 개수를 출력합니다.")
    private Integer badgeCount;

    @ArraySchema(schema = @Schema(implementation = BadgeRes.class))
    @Schema(description = "조회 뱃지 리스트")
    private List<BadgeRes> viewBadgeList;

    @ArraySchema(schema = @Schema(implementation = BadgeRes.class))
    @Schema(description = "저장 뱃지 리스트")
    private List<BadgeRes> saveBadgeList;
}
