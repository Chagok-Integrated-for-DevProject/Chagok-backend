package site.chagok.server.contest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "서버측 response 댓글 정보")
public class GetContestCommentDto {

    @Schema(description = "사용자 닉네임", example = "apple123")
    private String memberNickName;

    @Schema(description = "댓글 작성시간", example = "yyyy-mm-dd time")
    private LocalDateTime createdDate;

    @Schema(description = "댓글 내용", example = "팀원 구해요")
    private String content;

    @Schema(description = "댓글 ID", example = "130")
    private Long commentId;

    @Schema(description = "부모 댓글(상위 댓글) ID", example = "123 존재하지 않으면(대댓글이 아니라면) -1")
    private Long parentId;

    @Schema(description = "댓글의 카카오톡 연락주소, 댓글만(대댓글 x)", example = "open.kakao.com/...")
    private String kakaoRef;

    @Schema(description = "댓글 삭제 유무")
    private boolean deleted;

    @Schema(description = "대댓글 정보 GetContestCommentDto")
    private List<GetContestCommentDto> linkedComment;
}
