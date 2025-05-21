package bonda.bonda.domain.book.domain.repository;

import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.domain.BookCategory;
import bonda.bonda.domain.book.domain.QBook;
import bonda.bonda.domain.bookcase.QBookcase;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    private final QBook book = QBook.book;
    QBookcase bookcase = QBookcase.bookcase;

    @Override
    public Book queryDslInitTest(String name) { //queryDSL 테스트용 메소드
        Book testBook = jpaQueryFactory.selectFrom(book).
                where(book.title.eq(name)).fetchOne();
        return testBook;

    }

    @Override
    public Optional<Page<Book>> findBookListByCategory(Pageable pageable, String orderBy, String category) {
        BooleanBuilder predicate = new BooleanBuilder();

        // 카테고리 필터링
        if (!"ALL".equalsIgnoreCase(category)) {
            predicate.and(book.bookCategory.eq(BookCategory.valueOf(category.toUpperCase())));
        }
        // 정렬 조건
        if ("popularity".equalsIgnoreCase(orderBy)) {
            return findBooksOrderByPopularity(pageable, predicate);
        } else {
            return findBooksOrderByNewest(pageable, predicate);
        }
    }

    // 최신순 정렬 (일반 쿼리)
    private Optional<Page<Book>> findBooksOrderByNewest(Pageable pageable, BooleanBuilder predicate) {
        JPAQuery<Book> query = jpaQueryFactory
                .selectFrom(book)
                .where(predicate)
                .orderBy(book.publishDate.desc(), book.publisher.asc()); // 출판일 내림차순, 출판사 오름차순

        // 페이징 적용
        List<Book> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 (페이징용)
        Long total = jpaQueryFactory
                .select(book.count())
                .from(book)
                .where(predicate)
                .fetchOne();

        return Optional.of(new PageImpl<>(content, pageable, total != null ? total : 0));
    }

    private Optional<Page<Book>> findBooksOrderByPopularity(Pageable pageable, BooleanBuilder predicate) {
        JPQLQuery<Tuple> tupleQuery = jpaQueryFactory
                .select(book, bookcase.count().as("savedCount")) // 저장된 횟수 추가 조회 -> Tuple로 받음
                .from(book)
                .leftJoin(bookcase).on(bookcase.book.eq(book))
                .where(predicate)
                .groupBy(book.id)
                .orderBy(bookcase.count().desc(), book.publisher.asc());

        // 페이징 적용
        List<Tuple> tuples = tupleQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Tuple에서 Book 객체만 추출
        List<Book> content = tuples.stream()
                .map(t -> t.get(book))
                .toList();

        // 전체 카운트 (페이징용)
        Long total = jpaQueryFactory
                .select(book.count())
                .from(book)
                .where(predicate)
                .fetchOne();

        return Optional.of(new PageImpl<>(content, pageable, total != null ? total : 0));
    }
}