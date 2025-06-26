package bonda.bonda.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryCountDTO {

    private String category;

    private Long count;
}
