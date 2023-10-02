package site.chagok.server.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(description = "서버측 response 프로젝트 추천 정보")
public class GetRecommendedProjectDto {
    @Schema(description = "프로젝트 게시글 제목", example = "구합니다")
    private String title;
    @Schema(description = "프로젝트 게시글 ID", example = "330")
    @JsonProperty("id")
    private Long projectId;
}
