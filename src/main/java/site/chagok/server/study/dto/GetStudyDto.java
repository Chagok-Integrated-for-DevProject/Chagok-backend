package site.chagok.server.study.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chagok.server.common.contstans.SiteType;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Schema(description = "서버측 response 프로젝트 미리보기 정보")
public class GetStudyDto {

    @Schema(description = "스터디 게시글 ID", example = "330")
    @JsonProperty("id")
    private Long studyId;

    @Schema(description = "스터디 제목", example = "xx스터디 구해요")
    private String title;
    @Schema(description = "스터디 본문 (html 태그 포함)")
    private String content;

    @Schema(description = "스터디 url", example = "https://hoooollaaaa.com/...")
    private String sourceUrl;

    @Schema(description = "글 올린 시간", example = "2023-08-09 15:36:08.762")
    private LocalDateTime createdTime;
    @Schema(description = "스크랩 수", example = "13")
    private int scrapCount;
    @Schema(description = "조회수", example = "113")
    private int viewCount;
    @Schema(description = "사이트 종류", example = "OKKY or INFLEARN or HOLA")
    private SiteType siteType;
    @Schema(description = "기술스택", example = "String list")
    @JsonProperty("skills")
    private List<String> techStacks;

    @Schema(description ="스크랩 여부")
    private boolean scraped;
}
