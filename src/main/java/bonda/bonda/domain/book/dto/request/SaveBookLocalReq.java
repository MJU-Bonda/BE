package bonda.bonda.domain.book.dto.request;

import bonda.bonda.domain.book.domain.BookCategory;
import bonda.bonda.domain.book.domain.Subject;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SaveBookLocalReq {

    private String image;

    private String title;

    private String writer;

    private String publisher;

    private String size;

    private LocalDate publishDate;

    private Integer page;

    private String introduction;

    private String content;

    private BookCategory bookCategory;

    private Subject subject;
}
