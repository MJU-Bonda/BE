package bonda.bonda.domain.book.dto;

import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.dto.response.BookListRes;

import java.util.List;
import java.util.stream.Collectors;

public class BookMapper {
    public static List<BookListRes> convertToBookListRes(List<Book> bookList) {
        return bookList.stream()
                .map(book -> BookListRes.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .author(book.getWriter())
                        .imageUrl(book.getImage())
                        .category(book.getBookCategory() != null ? book.getBookCategory().getValue() : null)
                        .build())
                .collect(Collectors.toList());
    }
}
