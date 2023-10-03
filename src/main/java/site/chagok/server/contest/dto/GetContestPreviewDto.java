package site.chagok.server.contest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "서버측 response 공모전 미리보기 정보")
public class GetContestPreviewDto {

    @Schema(description = "공모전 id", example = "345")
    @JsonProperty("id")
    private Long contestId;
    @Schema(description = "공모전 제목", example = "2023년 공모")
    private String title;
    @Schema(description = "이미지 url", example = "https://abc.com/...")
    private String imageUrl;
    @Schema(description = "주최자", example = "xx사업단")
    private String host;
    @Schema(description = "모집 시작날짜", example = "yyyy-mm-dd")
    private LocalDate startDate;
    @Schema(description = "모집 종료날짜", example = "yyyy-mm-dd")
    private LocalDate endDate;
    @Schema(description = "스크랩 수", example = "13")
    private int scrapCount;
    @Schema(description = "댓글 수", example = "8")
    private int commentCount;
}
