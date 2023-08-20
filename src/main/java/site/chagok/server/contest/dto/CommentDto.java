package site.chagok.server.contest.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "CommentDto", description = "서버측 request 댓글 정보")
public class CommentDto {

    @ApiModelProperty(notes = "댓글 내용", example = "팀원 구함")
    private String content;

    @ApiModelProperty(notes = "부모 댓글(상위 댓글) Id, 존재하지 않으면 -1", example = "123 or -1")
    private Long parentId;

    @ApiModelProperty(notes = "공모전 글 id", example = "250")
    private Long contestId;

    @ApiModelProperty(notes = "카카오톡 연락주소 댓글만 가능, 대댓글 x ", example = "open.kakao.com/...")
    private String kakaoRef;
}
