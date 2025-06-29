package bonda.bonda.domain.article.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "아티클 검색 목록 읃답")
public class SearchArticleListRes {
    @Schema(description = "현재 요청 페이지")
    Integer page;
    @Schema(description = "전체 검색 결과 개수")
    Long total;
    @Schema(description = "현재 검색어")
    String word;
    @Schema(description = "현재 정렬 기준")
    String orderBy;
    @Schema(description = "다음 페이지 존재 여부")
    Boolean hasNextPage;
    @Schema(description = "아티클 리스트")
    List<SimpleArticleRes> articleList;
}
