package site.chagok.server.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chagok.server.common.contstans.SiteType;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ApiModel(value = "GetProjectPreviewDto", description = "서버측 response 프로젝트 미리보기 정보")
public class GetProjectPreviewDto {

    @ApiModelProperty(notes = "프로젝트 게시글 ID", example = "230")
    @JsonProperty("id")
    private Long projectId;
    @ApiModelProperty(notes = "사이트 종류", example = "OKKY or INFLEARN or HOLA")
    private SiteType siteType;
    @ApiModelProperty(notes = "프로젝트 제목", example = "xx프로젝트 모집합니다")
    private String title;
    @ApiModelProperty(notes = "조회수", example = "113")
    private int viewCount;
    @ApiModelProperty(notes = "스크랩 수", example = "13")
    private int scrapCount;
    @ApiModelProperty(notes = "미리보기", example = "xx 프로젝트 모집합니다. 현재...")
    private String preview;
    @ApiModelProperty(notes = "사용자닉네임", example = "apple123")
    private String nickName;

    @ApiModelProperty(notes = "기술스택", example = "String list")
    @JsonProperty("skills")
    private List<String> techStacks;
}
