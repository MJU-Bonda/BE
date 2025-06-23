package bonda.bonda.domain.article.application;

import bonda.bonda.domain.article.domain.Article;
import bonda.bonda.domain.article.domain.repository.ArticleRepository;
import bonda.bonda.domain.article.dto.ArticleMapper;
import bonda.bonda.domain.article.dto.response.SearchArticleListRes;
import bonda.bonda.domain.article.dto.response.SimpleArticleRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static bonda.bonda.domain.article.dto.ArticleMapper.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleSearchServiceImpl implements ArticleSearchService {
    private final ArticleRepository articleRepository;

    @Override
    public SuccessResponse<SearchArticleListRes> searchArticleList(int page, int size, String orderBy, String word, Member member) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articleList = articleRepository.searchArticleList(pageable, orderBy, word);
        List<SimpleArticleRes> ArticleListRes = convertToSimpleArticleListRes(articleList.getContent());

        return SuccessResponse.of(SearchArticleListRes.builder()
                .page(page)
                .total(articleList.getTotalElements())
                .word(word)
                .orderBy(orderBy)
                .hasNextPage(articleList.hasNext())
                .articleList(ArticleListRes)
                .build());
    }
}
