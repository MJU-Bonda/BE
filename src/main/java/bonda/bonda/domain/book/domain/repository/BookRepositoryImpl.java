package bonda.bonda.domain.book.domain.repository;

import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.domain.QBook;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    private final QBook book = QBook.book;

    @Override
    public Book queryDslInitTest(String name) {
        Book testBook = jpaQueryFactory.selectFrom(book).
                where(book.title.eq(name)).fetchOne();
        return testBook;

    }
}