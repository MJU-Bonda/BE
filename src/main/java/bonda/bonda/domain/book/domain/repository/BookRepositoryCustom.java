package bonda.bonda.domain.book.domain.repository;

import bonda.bonda.domain.book.domain.Book;


public interface BookRepositoryCustom {
    Book queryDslInitTest(String name); // QueryDsl 테스트 용 메소드
}
