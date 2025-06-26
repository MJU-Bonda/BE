package bonda.bonda.domain.article.dto.response;

import bonda.bonda.domain.book.dto.response.RelatedBookRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true) //추후 조합이 가능하도록 하기 위해
@Schema(description = "아티클 상세 응답 DTO")
public class ArticleDetailRes {
    @Schema(description = "아티클 아이디")
    Long articleId;
    @Schema(description = "아티클 제목")
    String title;
    @Schema(description = "아티클 소개글")
    String introduction;
    @Schema(description = "아티클 본문")
    String content;
    @Schema(description = "아티클 카테고리")
    String articleCategory;
    @Schema(description = "아티클 북마크 여부")
    Boolean isBookmarked;
    @Schema(description = "아티클 이미지 url")
    String imageUrl;
    @Schema(description = "뱃지 생성 여부")
    Boolean isNewBadge;
    @Schema(description = "연관된 도서 리스트")
    List<RelatedBookRes> relatedBookList;
    @Schema(description = "다른 아티클 리스트")
    List<SimpleArticleRes> otherArticleList;


}
