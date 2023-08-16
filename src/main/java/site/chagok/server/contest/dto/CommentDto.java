package site.chagok.server.contest.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel(value = "commentDto", description = "서버측 request 댓글 정보")
public class CommentDto {

    @ApiModelProperty(notes = "댓글 내용", example = "팀원 구함")
    private String content;

    @ApiModelProperty(notes = "부모 댓글(상위 댓글) Id", example = "123 존재하지 않으면 -1")
    private Long parentId;

    @ApiModelProperty(notes = "현재 댓글 Id", example = "130")
    private Long contestId;
}
