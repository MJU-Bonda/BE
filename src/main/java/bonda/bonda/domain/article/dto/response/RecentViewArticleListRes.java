package bonda.bonda.domain.article.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecentViewArticleListRes {
    Integer page;
    Boolean hasNextPage;
    List<SimpleArticleRes> articleList;
}
