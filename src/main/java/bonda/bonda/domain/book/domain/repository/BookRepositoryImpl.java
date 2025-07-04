package bonda.bonda.domain.book.domain.repository;

import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.domain.BookCategory;
import bonda.bonda.domain.book.domain.QBook;
import bonda.bonda.domain.book.domain.Subject;
import bonda.bonda.domain.book.dto.response.BookDetailRes;
import bonda.bonda.domain.bookcase.QBookcase;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.QMember;
import bonda.bonda.domain.recentviewbook.QRecentViewBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QBook book = QBook.book;
    private final QBookcase bookcase = QBookcase.bookcase;
    private final QMember member = QMember.member;
    private final QRecentViewBook recentViewBook = QRecentViewBook.recentViewBook;

    @Override
    public Book queryDslInitTest(String name) { //테스트용 메서드
        return jpaQueryFactory
                .selectFrom(book)
                .where(book.title.eq(name))
                .fetchOne();
    }

    @Override
    public Optional<Page<Book>> findBookListByCategory(Pageable pageable, String orderBy, String category) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (!"ALL".equalsIgnoreCase(category)) { //카테고리 조건
            predicate.and(book.bookCategory.eq(BookCategory.valueOf(category.toUpperCase())));
        }
        return Optional.of(fetchBookPage(pageable, predicate, orderBy)); //정렬조건 + 페이징 조건 추가
    }

    @Override
    public Optional<Page<Book>> searchBookList(Pageable pageable, String orderBy, String word) {
        if (word == null || word.isBlank()) { // 빈칸이거나 공백이면 결과 없음 처리
            return Optional.of(new PageImpl<>(List.of(), pageable, 0));
        }

        BooleanBuilder predicate = new BooleanBuilder();
        Arrays.stream(word.trim().split("\\s+")) // 앞뒤 공백 제거 + 공백으로 단어 나누기 [푸른,하늘]
                .forEach(w -> predicate.and(book.title.containsIgnoreCase(w))); //단어 돌면서 and 조건 추가 == 교집합

        return Optional.of(fetchBookPage(pageable, predicate, orderBy)); //정렬조건 + 페이징 조건 추가
    }

    @Override
    public List<Book> findLovedBookList(String subject) {
        BooleanBuilder predicate = new BooleanBuilder();
        //주제 필터 조건
        if (!"ALL".equalsIgnoreCase(subject)) { //카테고리 조건
            predicate.and(book.subject.eq(Subject.valueOf(subject.toUpperCase())));
        }
        //페이지 조건 (상위 3개)
        Pageable pageable = PageRequest.of(0, 3);
        return fetchBooksByPopularityThenRandom(pageable, predicate);
    }


    @Override
    public Page<Book> findMySavedBookList(Pageable pageable, String orderBy, Member loginMember) {
        BooleanBuilder predicate = new BooleanBuilder();
        return fetchBookPage(pageable, predicate, orderBy, loginMember);
    }

    @Override
    public BookDetailRes findBookDetailResWithIsBookMarked(Long bookId, Member member) {
        Tuple tuple = jpaQueryFactory
                .select(
                        book.bookCategory,
                        book.title,
                        book.image,
                        book.writer,
                        book.publisher,
                        book.size,
                        book.page,
                        book.subject,
                        book.introduction,
                        book.content,
                        bookcase.id.isNotNull()
                )
                .from(book)
                .leftJoin(bookcase).on(bookcase.book.eq(book).and(bookcase.member.eq(member)))
                .where(book.id.eq(bookId))
                .fetchOne();
        return BookDetailRes.builder()
                .bookId(bookId)
                .category(tuple.get(book.bookCategory))
                .title(tuple.get(book.title))
                .imageUrl(tuple.get(book.image))
                .author(tuple.get(book.writer))
                .publisher(tuple.get(book.publisher))
                .plateType(tuple.get(book.size))
                .page(tuple.get(book.page))
                .subject(tuple.get(book.subject))
                .introduction(tuple.get(book.introduction))
                .content(tuple.get(book.content))
                .isBookmarked(tuple.get(bookcase.id.isNotNull()))
                .isNewBadge(false) // 나중에 세팅
                .related_article_list(Collections.emptyList()) // 나중에 세팅
                .build();
    }

    @Override
    public Page<Book> findRecentViewBookList(Member member, Pageable pageable) {
        return fetchRecentBooksPageByViewDate(pageable, new BooleanBuilder(), member);
    }

    @Override
    public List<Book> findRecentBookListBySubject(String subject) {
        BooleanBuilder predicate = new BooleanBuilder();
        //주제 필터 조건
        if (!"ALL".equalsIgnoreCase(subject)) { //카테고리 조건
            predicate.and(book.subject.eq(Subject.valueOf(subject.toUpperCase())));
        }
        //페이지 조건 (상위 3개)
        Pageable pageable = PageRequest.of(0, 3);
        return fetchBooksByPublishDateThenRandom(pageable, predicate);
    }

    @Override
    public Map<BookCategory, Long> countBookcaseByCategory(Member member) {
        List<Tuple> results = jpaQueryFactory
                .select(book.bookCategory, bookcase.count())
                .from(bookcase)
                .join(bookcase.book, book)
                .where(bookcase.member.eq(member))
                .groupBy(book.bookCategory)
                .fetch();

        return results.stream()
                .filter(t -> t.get(book.bookCategory) != null && t.get(bookcase.count()) != null)
                .collect(Collectors.toMap(
                        t -> t.get(book.bookCategory),
                        t -> t.get(bookcase.count())
                ));
    }




    // === 내부 공통 메서드 ===

// == 정렬 기준 설정 ==
    // 1) loginMember 없는 버전
    private Page<Book> fetchBookPage(Pageable pageable, BooleanBuilder predicate, String orderBy) {
        return fetchBookPage(pageable, predicate, orderBy, null);
    }

    // 2) loginMember 있는 버전
    private Page<Book> fetchBookPage(Pageable pageable, BooleanBuilder predicate, String orderBy, Member loginMember) {
        List<Book> content;
        switch (orderBy.toLowerCase()) {
            case "popularity": // 인기순 정렬
                content = fetchBooksByPopularity(pageable, predicate);
                break;
            case "recentlysaved": //최근담은순 정렬
                content = fetchBooksByRecentlySaved(pageable, predicate, loginMember);
                break;
            case "title": //제목순 정렬
                content = fetchBooksByTitle(pageable, predicate, loginMember);
                break;
            default: //기본 최근순 (출판일)
                content = fetchBooksByNewest(pageable, predicate); // 기본값
                break;
        }

        return createPage(content, pageable, predicate, loginMember); // 페이지 조건 추가
    }


    // ==기본 반환 (List 반환)==
    private Page<Book> fetchRecentBooksPageByViewDate(Pageable pageable, BooleanBuilder predicate, Member loginMember) {
        // 1. 조건에 맞는 도서 리스트 조회 (최근 본 순서대로 정렬, 페이징)
        List<Book> bookList = jpaQueryFactory
                .selectFrom(book)
                .innerJoin(recentViewBook).on(
                        recentViewBook.member.eq(loginMember),
                        recentViewBook.book.eq(book)
                )
                .where(predicate)
                .orderBy(recentViewBook.viewDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

// 2. 전체 개수 계산 (hasNext() 등 페이지 정보 생성을 위해)
        Long total = jpaQueryFactory
                .select(book.count())
                .from(book)
                .innerJoin(recentViewBook).on(
                        recentViewBook.book.eq(book),
                        recentViewBook.member.eq(loginMember)
                )
                .where(predicate)
                .fetchOne();

// 3. Page 객체로 결과 구성 (total 값이 null일 경우 0 처리)
        return new PageImpl<>(bookList, pageable, total != null ? total : 0);
    }

    // 인기 순 정렬
    private List<Book> fetchBooksByPopularity(Pageable pageable, BooleanBuilder predicate) {
        List<Tuple> tuples = jpaQueryFactory
                .select(book, bookcase.count().as("savedCount"))  // 저장된 횟수 추가 조회 -> Tuple로 받음
                .from(book)
                .leftJoin(bookcase).on(bookcase.book.eq(book))
                .where(predicate)
                .groupBy(book.id)
                .orderBy(bookcase.count().desc(), book.publisher.asc()) // 인기순 내림차순, 출판사 오름차순
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return tuples.stream()
                .map(t -> t.get(book))
                .toList();
    }
    // 최근 담은 순 정렬
    private List<Book> fetchBooksByRecentlySaved(Pageable pageable, BooleanBuilder predicate, Member loginMember) {
        List<Tuple> tuples = jpaQueryFactory
                .select(book, bookcase._super.createdAt.as("savedAt"))  // 저장된 횟수 추가 조회 -> Tuple로 받음
                .from(book)
                .leftJoin(bookcase).on(bookcase.book.eq(book))
                .leftJoin(bookcase.member, member) //
                .where(member.eq(loginMember))
                .where(predicate)
                .orderBy(bookcase._super.createdAt.desc()) // 저장된 책 생성 기준 내림차순
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return tuples.stream()
                .map(t -> t.get(book))
                .toList();
    }
    //제목 순 정렬
    private List<Book> fetchBooksByTitle(Pageable pageable, BooleanBuilder predicate, Member loginMember) {
        OrderSpecifier<Integer> customOrder = new CaseBuilder()
                .when(book.title.substring(0, 1).between("가", "힣")).then(1) // 가나다 1순위
                .when(book.title.substring(0, 1).between("A", "Z")).then(2) // 영어 대문자 2순위
                .when(book.title.substring(0, 1).between("a", "z")).then(3) // 영어 소문자 3순위
                .otherwise(4) //나머지 (특수문자) 4순위
                .asc();

        return jpaQueryFactory
                .selectFrom(book)
                .leftJoin(bookcase).on(bookcase.book.eq(book))
                .leftJoin(bookcase.member, member) //
                .where(member.eq(loginMember))
                .where(predicate)
                .orderBy(
                        customOrder, // 커스텀 정렬 순위
                        book.title.asc(), // 정렬 순위에 대해 제목에 적용
                        book.publishDate.desc() // 제목 같은 경우 출판일 기준 내림차순
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
    // 기본 최신순 정렬 (출판일)
    private List<Book> fetchBooksByNewest(Pageable pageable, BooleanBuilder predicate) {
        return jpaQueryFactory  // 책 객체로 받기
                .selectFrom(book)
                .where(predicate)
                .orderBy(book.publishDate.desc(), book.publisher.asc()) // 출판일 내림차순, 출판사 오름차순
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
    // 최근 사랑받는 도서 목록 조건
    private List<Book> fetchBooksByPopularityThenRandom(Pageable pageable, BooleanBuilder predicate) {
        List<Tuple> tuples = jpaQueryFactory
                .select(book, bookcase.count().as("savedCount"))
                .from(book)
                .leftJoin(bookcase).on(bookcase.book.eq(book)) //
                .leftJoin(bookcase.member, member) //
                .where(predicate)
                .groupBy(book.id)
                .orderBy(bookcase.count().desc(), //인기 순 정렬
                        Expressions.numberTemplate(Double.class, "rand()").asc()) // 같으면 랜덤 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return tuples.stream()
                .map(t -> t.get(book))
                .toList();
    }
    private List<Book> fetchBooksByPublishDateThenRandom(Pageable pageable, BooleanBuilder predicate) {
        return jpaQueryFactory
                .selectFrom(book)
                .where(predicate)
                .orderBy(
                        book.publishDate.desc(), // 출판일 기준 내림차순
                        Expressions.numberTemplate(Double.class, "rand()").asc() // 동일한 출판일이면 랜덤 정렬
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    // == 페이지 기준 설정 ==
    private Page<Book> createPage(List<Book> content, Pageable pageable, BooleanBuilder predicate, Member loginMember) {
        BooleanBuilder totalPredicate = new BooleanBuilder(predicate);
        // 현재 회원이 선택한 것만 필요할 때, 조건 추가
        if (loginMember != null) {
            totalPredicate.and(bookcase.member.eq(loginMember));
        }

        Long total = jpaQueryFactory
                .select(book.count())
                .from(book)
                .leftJoin(bookcase).on(bookcase.book.eq(book))  // bookcase 조인 필수!
                .where(totalPredicate)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
