package site.chagok.server.study.dto;

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
@ApiModel(value = "GetStudyPreviewDto", description = "서버측 response 스터디 미리보기 정보")
public class GetStudyPreviewDto {
    @ApiModelProperty(notes = "스터디 게시글 ID", example = "330")
    @JsonProperty("id")
    private Long studyId;
    @ApiModelProperty(notes = "스터디 제목", example = "xx스터디 구해요")
    private String title;
    @ApiModelProperty(notes = "사이트 종류", example = "OKKY or INFLEARN or HOLA")
    private SiteType siteType;
    @ApiModelProperty(notes = "기술스택", example = "String list")
    @JsonProperty("skills")
    private List<String> techStacks;
    @ApiModelProperty(notes = "조회수", example = "113")
    private int viewCount;
    @ApiModelProperty(notes = "스크랩 수", example = "13")
    private int scrapCount;
    @ApiModelProperty(notes = "미리보기", example = "xx 스터디 모집합니다. 현재...")
    private String preview;
    @ApiModelProperty(notes = "사용자닉네임", example = "apple123")
    private String nickName;
}
