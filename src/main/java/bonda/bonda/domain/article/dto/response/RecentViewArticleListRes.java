package bonda.bonda.domain.article.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "최근 조회한 아티클 목록 응답 DTO")
public class RecentViewArticleListRes {
    @Schema(description = "요청 페이지", example = "0")
    Integer page;
    @Schema(description = "다음 페이지 존재 여부", example = "false")
    Boolean hasNextPage;
    @Schema(description = "아티클 리스트 목록 dto")
    List<SimpleArticleRes> articleList;
}
