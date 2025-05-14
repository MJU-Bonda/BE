package bonda.bonda.domain.book.domain.repository;

import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.domain.BookCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BookQueryDslInitTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    @Transactional
    public void BookQueryDslInitTest() {
        //given
        Book testBook = Book.builder()
                .title("test")
                .bookCategory(BookCategory.CATEGORY)
                .build();
        bookRepository.save(testBook);

        //when
        Book findBook = bookRepository.queryDslInitTest("test"); // 제목으로 조회함

        //then
        Assertions.assertThat(findBook.getTitle()).isEqualTo("test");
    }
}
