package bonda.bonda.domain.article.domain.repository;

import bonda.bonda.domain.article.domain.Article;
import bonda.bonda.domain.article.domain.ArticleCategory;
import bonda.bonda.domain.article.domain.QArticle;
import bonda.bonda.domain.article.dto.response.ArticleDetailRes;
import bonda.bonda.domain.article.dto.response.SimpleArticleRes;
import bonda.bonda.domain.article.dto.response.SimpleArticleResWithBookmarked;
import bonda.bonda.domain.articlecase.QArticlecase;
import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.domain.QBook;
import bonda.bonda.domain.book.dto.response.RelatedBookRes;
import bonda.bonda.domain.bookarticle.QBookArticle;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.QMember;
import bonda.bonda.domain.recentviewarticle.QRecentViewArticle;
import bonda.bonda.domain.recentviewbook.QRecentViewBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QArticle article = QArticle.article;
    private final QArticlecase articlecase = QArticlecase.articlecase;
    private final QBookArticle bookArticle = QBookArticle.bookArticle;
    private final QBook book = QBook.book;
    private final QMember member = QMember.member;
    private final QRecentViewArticle recentViewArticle = QRecentViewArticle.recentViewArticle;

    @Override
    public Page<SimpleArticleResWithBookmarked> findArticleListByCategory(Pageable pageable, String category, Member loginMember) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (!"ALL".equalsIgnoreCase(category)) { //카테고리 조건 -> ALL 이면 무시
            predicate.and(article.articleCategory.eq(ArticleCategory.valueOf(category.toUpperCase())));
        }

        List<SimpleArticleResWithBookmarked> articles = jpaQueryFactory
                .select(Projections.constructor( //응답 dto로 조회
                        SimpleArticleResWithBookmarked.class,
                        article.id,
                        article.articleCategory.stringValue(),
                        article.title,
                        article.introduction,
                        article.image,
                        JPAExpressions.selectOne()
                                .from(articlecase)
                                .where(articlecase.member.eq(loginMember).and(articlecase.article.eq(article)))
                                .exists()
                ))
                .from(article)
                .where(predicate)
                .orderBy(article.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(article.count())
                .from(article)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(articles, pageable, total != null ? total : 0);
    }

    /**
     *  아티클 리스트를 검색어로 검색합니다. 검색어 조근을 만들어서, 정렬 처리 해주는 메서드를 반환합니다.
     * **/
    @Override
    public Page<Article> searchArticleList(Pageable pageable, String orderBy, String word) {
        if (word == null || word.isBlank()) { // 빈칸이거나 공백이면 결과 없음 처리
            return new PageImpl<>(List.of(), pageable, 0);
        }
        BooleanBuilder predicate = new BooleanBuilder();

        // 검색 조건 추가
        Arrays.stream(word.trim().split("\\s+")) // 앞 뒤 공백 지우고, 공백 기준 쪼개기
                .forEach(w -> { // 단어 돌면서
                    BooleanBuilder wordPredicate = new BooleanBuilder();
                    wordPredicate.or(article.title.containsIgnoreCase(w)) //아티클 제목
                            .or(article.content.containsIgnoreCase(w)) // 아티클 내용
                            .or(article.introduction.containsIgnoreCase(w)) // 아티클 소개글
                            .or(book.title.containsIgnoreCase(w)); // 연관된 책의 제목
                    predicate.and(wordPredicate);
                });

        //orderBy에 따라 메서드 분리
        return fetchArticlePage(pageable, predicate, orderBy, null);
    }

    /**
     * 요청 멤버가 저장한 아티클 리스트를 반환합니다. 입력되는 정렬 기준에 따라 해당 메서드를 호출합니다.
     */
    @Override
    public Page<Article> findMySavedArticleList(Pageable pageable, String orderBy, Member loginMember) {
        BooleanBuilder predicate = new BooleanBuilder();
        return fetchArticlePage(pageable, predicate, orderBy, loginMember);
    }

    @Override

    public ArticleDetailRes getArticleDetail(Article article, Member loginMember) {
        ArticleDetailRes articleDetailRes = getArticleBase(article, loginMember);
        List<RelatedBookRes> relatedBooks = getRelatedBooks(article);
        List<SimpleArticleRes> otherArticleList = getOtherArticleList(article);
        return articleDetailRes.toBuilder()
                .relatedBookList(relatedBooks)
                .otherArticleList(otherArticleList)
                .build();
    }

    /**
     * 다른 큐레이션을 조회합니다
     */
    private List<SimpleArticleRes> getOtherArticleList(Article persistArticle) {
        // 3. 다음 3개 아티클 조회 (순환)
        List<SimpleArticleRes> afterList = jpaQueryFactory
                .select(Projections.constructor(SimpleArticleRes.class,
                        article.id,
                        article.title,
                        article.articleCategory.stringValue(),
                        article.image
                ))
                .from(article)
                .where(article.id.gt(persistArticle.getId())) //현재보다 greater than
                .orderBy(article.id.asc())
                .limit(3)
                .fetch();
        // 만약 3개가 아닌 경우 (마지막 인덱스를 지난 경우 1번부터 채우기)
        int remain = 3 - afterList.size();
        List<SimpleArticleRes> beforeList = Collections.emptyList();

        if (remain > 0) {
            beforeList = jpaQueryFactory
                    .select(Projections.constructor(SimpleArticleRes.class,
                            article.id,
                            article.title,
                            article.articleCategory.stringValue(),
                            article.image
                    ))
                    .from(article)
                    .where(article.id.lt(persistArticle.getId())) //less than -> 본인 제외
                    .orderBy(article.id.asc())
                    .limit(remain)
                    .fetch();
        }

        List<SimpleArticleRes> otherArticleList = new ArrayList<>(afterList);
        otherArticleList.addAll(beforeList);
        return otherArticleList;
    }

    /**
     * 해당 아티클과 연관된 4개의 도서 정보를 가져옵니다.
     */
    private List<RelatedBookRes> getRelatedBooks(Article persistArticle) {
        // 2. RelatedBook 리스트 조회
        return jpaQueryFactory
                .select(Projections.constructor(RelatedBookRes.class,
                        book.id,
                        book.title,
                        book.writer,
                        book.bookCategory,
                        book.introduction,
                        book.content
                ))
                .from(bookArticle)
                .join(bookArticle.book, book)
                .where(bookArticle.article.id.eq(persistArticle.getId()))
                .fetch();

    }

    /**
     * 아티클의 기본 정보를 가져옵니다. 추후, 연관 아티클 및 도서를 추가합니다
     */
    private ArticleDetailRes getArticleBase(Article persistArticle, Member loginMember) {
        // 1. 아티클 단건 조회 + 북마크 여부 포함
        Tuple articleTuple = jpaQueryFactory
                .select(
                        article.id,
                        article.title,
                        article.introduction,
                        article.content,
                        article.articleCategory.stringValue(),
                        article.image,
                        articlecase.id.isNotNull()
                )
                .from(article)
                .leftJoin(articlecase)
                .on(articlecase.member.eq(loginMember).and(articlecase.article.eq(article)))
                .where(article.id.eq(persistArticle.getId()))
                .fetchOne();


        return ArticleDetailRes.builder()
                .articleId(persistArticle.getId())
                .title(articleTuple.get(article.title))
                .introduction(articleTuple.get(article.introduction))
                .content(articleTuple.get(article.content))
                .articleCategory(articleTuple.get(article.articleCategory.stringValue()))
                .isBookmarked(articleTuple.get(articlecase.id.isNotNull()))
                .imageUrl(articleTuple.get(article.image))
                .build();

    }

    @Override
    public Page<Article> findRecentViewArticleList(Member loginMember, Pageable pageable) {
        // 1. 조건에 맞는 아티클 리스트 조회 (최근 본 순서대로 정렬, 페이징)
        List<Article> articleList = jpaQueryFactory
                .selectFrom(article)
                .innerJoin(recentViewArticle).on(
                        recentViewArticle.member.eq(loginMember),
                        recentViewArticle.article.eq(article)
                )
                .orderBy(recentViewArticle.viewDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

// 2. 전체 개수 계산 (hasNext() 등 페이지 정보 생성을 위해)
        Long total = jpaQueryFactory
                .select(article.count())
                .from(article)
                .innerJoin(recentViewArticle).on(
                        recentViewArticle.article.eq(article),
                        recentViewArticle.member.eq(loginMember)
                )
                .fetchOne();

// 3. Page 객체로 결과 구성 (total 값이 null일 경우 0 처리)
        return new PageImpl<>(articleList, pageable, total != null ? total : 0);

    }

    /**
     *
     * orderBy 속성에 따라서 메서드를 분기 처리해줍니다.
     */
    private Page<Article> fetchArticlePage(Pageable pageable, BooleanBuilder predicate, String orderBy, Member loginMember) {
        List<Article> content = switch (orderBy.toLowerCase()) {
            case "popularity" -> // 인기순 정렬
                    fetchArticlesByPopularity(pageable, predicate);
            case "recentlysaved" -> // 최근 담은 순 정렬
                    fetchArticlesByRecentlySaved(pageable, predicate, loginMember);
            case "title" ->// 제목 순 정렬
                    fetchArticlesByTitle(pageable, predicate, loginMember);
            default -> // newest
                    fetchArticlesByNewest(pageable, predicate);
        };
        return createPage(content, pageable, predicate, loginMember);
    }

    /**
     * 로그인한 멤버가 저장한 아티클 리스트를, 아티클 제목 기준 내림차순으로 정렬합니다.
     */
    private List<Article> fetchArticlesByTitle(Pageable pageable, BooleanBuilder predicate, Member loginMember) {
        OrderSpecifier<Integer> customOrder = new CaseBuilder()
                .when(article.title.substring(0, 1).between("가", "힣")).then(1) // 가나다 1순위
                .when(article.title.substring(0, 1).between("A", "Z")).then(2) // 영어 대문자 2순위
                .when(article.title.substring(0, 1).between("a", "z")).then(3) // 영어 소문자 3순위
                .otherwise(4) //나머지 (특수문자) 4순위
                .asc();

        return jpaQueryFactory
                .selectFrom(article)
                .leftJoin(articlecase).on(articlecase.article.eq(article))
                .leftJoin(articlecase.member, member) //
                .where(member.eq(loginMember))
                .where(predicate)
                .orderBy(
                        customOrder, // 커스텀 정렬 순위
                        article.title.asc(), // 정렬 순위에 대해 제목에 적용
                        article.createdAt.desc() // 제목 같은 경우 생성된 시점 기준 내림차순
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
    /**
     * 로그인한 멤버가 저장한 아티클 리스트를, 저장한 순 기준 내림차순으로 정렬합니다.
     */
    private List<Article> fetchArticlesByRecentlySaved(Pageable pageable, BooleanBuilder predicate, Member loginMember) {
        List<Tuple> tuples = jpaQueryFactory
                .select(article, articlecase._super.createdAt.as("savedAt"))  // 저장된 횟수 추가 조회 -> Tuple로 받음
                .from(article)
                .leftJoin(articlecase).on(articlecase.article.eq(article))
                .leftJoin(articlecase.member, member) //
                .where(member.eq(loginMember))
                .where(predicate)
                .orderBy(articlecase._super.createdAt.desc()) // 저장된 아티클 생성 기준 내림차순
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return tuples.stream()
                .map(t -> t.get(article))
                .toList();
    }

    /**
     * 저장한 아티클과 추가 조인해서, 아티클을 최신순으로 정렬합니다.
     */
    private List<Article> fetchArticlesByPopularity(Pageable pageable, BooleanBuilder predicate) {
        List<Tuple> tuples = jpaQueryFactory
                .select(article, articlecase.count().as("savedCount")) // 중복 제거
                .from(article)
                .leftJoin(bookArticle).on(bookArticle.article.eq(article)) // 아티클 기준 조인
                .leftJoin(bookArticle.book, book) // 도서 조인
                .leftJoin(articlecase).on(articlecase.article.eq(article))
                .where(predicate)
                .groupBy(article.id)
                .orderBy(articlecase.count().desc(),
                        Expressions.numberTemplate(Double.class, "rand()").asc()) // 같으면 랜덤 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return tuples.stream()
                .map(t -> t.get(article))
                .toList();
    }


    /**
     *  최근 생성일 기준으로 정렬하여 아티클 리스트를 조회합니다.
     * **/
    private List<Article> fetchArticlesByNewest(Pageable pageable, BooleanBuilder predicate) {
        return jpaQueryFactory
                .selectDistinct(article) // 중복 제거
                .from(article)
                .leftJoin(bookArticle).on(bookArticle.article.eq(article)) // 아티클 기준 조인
                .leftJoin(bookArticle.book, book) // 도서 조인
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(article.createdAt.desc())
                .fetch();
    }

    /**
     *  pageable 정보를 받아서, next page 여부를 확인하기 위해 count() 쿼리를 추가로 보냅니다.
     * **/
    private Page<Article> createPage(List<Article> content, Pageable pageable, BooleanBuilder predicate, Member loginMember) {
        BooleanBuilder totalPredicate = new BooleanBuilder(predicate);
        // 현재 회원이 선택한 것만 필요할 때, 조건 추가
        if (loginMember != null) {
            totalPredicate.and(articlecase.member.eq(loginMember));
        }

        Long total = jpaQueryFactory
                .select(article.count())
                .from(article)
                .leftJoin(articlecase).on(articlecase.article.eq(article))  // bookcase 조인 필수!
                .where(totalPredicate)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

}