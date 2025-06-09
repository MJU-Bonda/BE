package bonda.bonda.domain.member.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyPageInfoRes {

    private String nickname;

    private String profileImage;

    private Integer savedBookCount;

    private Integer badgeCount;
}
