package bonda.bonda.domain.badge.dto.response;

import bonda.bonda.domain.badge.domain.ProgressType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BadgeDetailsRes {

    private String name;

    private String description;

    private ProgressType progressType;

    private Integer currentProgress;

    private Integer goal;

    private Boolean isUnlocked;

    private LocalDate acquiredDate;
}
