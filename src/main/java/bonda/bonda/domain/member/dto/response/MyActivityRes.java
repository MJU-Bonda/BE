package bonda.bonda.domain.member.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MyActivityRes {

    private Integer bookViewCount;

    private Integer bookcaseCount;

    private List<CategoryCountDTO> categoryCountList;
}
