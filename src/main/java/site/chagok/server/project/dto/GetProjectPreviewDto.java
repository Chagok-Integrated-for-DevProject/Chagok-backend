package site.chagok.server.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chagok.server.common.contstans.PostType;
import site.chagok.server.common.contstans.SiteType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Schema(description = "서버측 response 프로젝트 미리보기 정보")
public class GetProjectPreviewDto {

    @Schema(description = "프로젝트 게시글 ID", example = "230")
    @JsonProperty("id")
    private Long projectId;
    @Schema(description = "사이트 종류", example = "OKKY or INFLEARN or HOLA")
    private SiteType siteType;
    @Schema(description = "글 종류", example = "Project or study")
    private PostType postType;
    @Schema(description = "프로젝트 제목", example = "xx프로젝트 모집합니다")
    private String title;
    @Schema(description = "조회수", example = "113")
    private int viewCount;
    @Schema(description = "스크랩 수", example = "13")
    private int scrapCount;
    @Schema(description = "미리보기", example = "xx 프로젝트 모집합니다. 현재...")
    private String preview;
    @Schema(description = "글 올린 시간", example = "2023-08-09 15:36:08.762")
    private LocalDateTime createdTime;

    @Schema(description = "기술스택", example = "String list")
    @JsonProperty("skills")
    private Set<String> techStacks;
}
