package site.chagok.server.study.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "서버측 response 스터디 추천 정보")
public class GetRecommendedStudyDto {
    @Schema(description = "스터디 게시글 제목", example = "구합니다")
    private String title;
    @Schema(description = "스터디 게시글 ID", example = "330")
    @JsonProperty("id")
    private Long studyId;
}
