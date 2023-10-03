package site.chagok.server.contest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "서버측 request 업데이트 댓글 정보")
public class CommentUpdateDto {

    @Schema(description = "댓글 ID", example = "130")
    private Long commentId;

    @Schema(description = "댓글 내용", example = "팀원 구함")
    private String content;

    @Schema(description = "댓글의 카카오톡 연락주소, 댓글만(대댓글 x)", example = "open.kakao.com/...")
    private String kakaoRef;
}
