package site.chagok.server.study.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetRecommendedStudyDto {
    @ApiModelProperty(notes = "스터디 게시글 제목", example = "구합니다")
    private String title;
    @ApiModelProperty(notes = "스터디 게시글 ID", example = "330")
    @JsonProperty("id")
    private Long studyId;
}
