package bonda.bonda.domain.badge.dto.response;

import bonda.bonda.domain.badge.domain.ProgressType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BadgeDetailsRes {

    @Schema(type = "String", example = "느긋한 발걸음", description = "뱃지의 이름을 출력합니다.")
    private String name;

    @Schema(type = "String", example = "첫 도서 상세페이지를 조회하면 획득할 수 있어요.", description = "뱃지의 설명을 출력합니다. 획득 시 설명이 변경됩니다.")
    private String description;

    @Schema(type = "Enum", example = "BOOK_VIEW", description = "뱃지의 타입을 BOOK_VIEW, BOOK_SAVE, ARTICLE_VIEW, ARTICLE_SAVE로 구분합니다.")
    private ProgressType progressType;

    @Schema(type = "Integer", example = "1", description = "현재 진척도를 출력합니다.")
    private Integer currentProgress;

    @Schema(type = "Integer", example = "1", description = "뱃지를 얻기 위한 목표치를 출력합니다.")
    private Integer goal;

    @Schema(type = "Boolean", example = "true", description = "뱃지 획득 여부를 출력합니다. true면 회원이 뱃지를 획득했음을 의미합니다.")
    private Boolean isUnlocked;

    @Schema(type = "LocalDate", example = "2025-01-01", description = "뱃지 획득 날짜를 출력합니다.")
    private LocalDate acquiredDate;
}
