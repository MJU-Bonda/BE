package bonda.bonda.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KakaoMemberRes {

    @Schema(type = "String", example = "1234567890", description = "카카오 사용자의 아이디 번호입니다.")
    private String kakaoId;
}
