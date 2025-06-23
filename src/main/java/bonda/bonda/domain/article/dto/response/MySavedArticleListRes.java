package bonda.bonda.domain.article.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MySavedArticleListRes {
    Integer page;
    Long total;
    String orderBy;
    Boolean hasNextPage;
    List<SimpleArticleRes> articleList;

}
