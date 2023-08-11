package site.chagok.server.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardScrap {

    // 게시판 스크랩 DTO

    // 카테고리는 ( study, project, contest )

    @ApiParam(value = "스크랩 글 category, ( study, project, contest )만 가능")
    @ApiModelProperty(example = "study or project or contest")
    String category;

    @JsonProperty(value="id")
    @ApiParam(value = "게시글 id")
    Long boardId;
}
