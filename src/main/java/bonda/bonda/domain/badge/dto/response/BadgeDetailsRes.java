package bonda.bonda.domain.badge.dto.response;

import bonda.bonda.domain.badge.domain.ProgressType;

public class BadgeDetailsRes {

    private String name;

    private String description;

    private String image;

    private ProgressType progressType;

    private Integer goal;

    private Boolean isUnlocked;
}
