package site.chagok.server.contest.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "서버측 request 댓글 정보")
public class CommentDto {

    @Schema(description = "댓글 내용", example = "팀원 구함")
    private String content;

    @Schema(description = "부모 댓글(상위 댓글) Id, 존재하지 않으면 -1", example = "123 or -1")
    private Long parentId;

    @Schema(description = "공모전 글 id", example = "250")
    private Long contestId;

    @Schema(description = "카카오톡 연락주소 댓글만 가능, 대댓글 x ", example = "open.kakao.com/...")
    private String kakaoRef;


}
