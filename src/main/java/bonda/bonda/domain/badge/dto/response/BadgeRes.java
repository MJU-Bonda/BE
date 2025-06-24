package bonda.bonda.domain.badge.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BadgeRes {

    private Long Id;

    private String name;

    private String image;

    private Boolean isUnlocked;
}
