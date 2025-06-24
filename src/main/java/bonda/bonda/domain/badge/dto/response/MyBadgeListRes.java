package bonda.bonda.domain.badge.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MyBadgeListRes {

    private Integer badgeCount;

    private List<BadgeRes> viewBadgeList;

    private List<BadgeRes> saveBadgeList;
}
