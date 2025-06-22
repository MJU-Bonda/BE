package bonda.bonda.domain.article.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "카테고리별 아티클 조회 응답")
public class ArticleListByCategoryRes {
    @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
    Integer page;
    @Schema(description = "조회한 카테고리", example = "ALL")
    String category;
    @Schema(description = "다음 페이지 존재 여부", example = "true")
    Boolean hasNextPage;
    @Schema(description = "아티클 목록", implementation = SimpleArticleResWithBookmarked.class)
    List<SimpleArticleResWithBookmarked> articleList;
}
