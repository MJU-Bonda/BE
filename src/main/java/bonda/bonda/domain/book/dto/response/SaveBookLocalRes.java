package bonda.bonda.domain.book.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveBookLocalRes {

    private Long id;

    private String image;

    private String title;
}
