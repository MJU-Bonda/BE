package bonda.bonda.domain.badge.domain;

import bonda.bonda.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "badge")
public class Badge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String image;

    @Enumerated(EnumType.STRING)
    private ProgressType progressType;  // 진척도 계산 타입

    private Integer goal;               // 목표치

    @Builder
    public Badge(String name, String description, String image, ProgressType progressType, Integer goal) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.progressType = progressType;
        this.goal = goal;
    }
}
