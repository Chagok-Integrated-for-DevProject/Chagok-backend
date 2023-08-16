package site.chagok.server.contest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "GetContestPreviewDto", description = "서버측 response 공모전 미리보기 정보")
public class GetContestPreviewDto {

    @ApiModelProperty(notes = "공모전 id", example = "345")
    @JsonProperty("id")
    private Long contestId;
    @ApiModelProperty(notes = "공모전 제목", example = "2023년 공모")
    private String title;
    @ApiModelProperty(notes = "이미지 url", example = "https://abc.com/...")
    private String imageUrl;
    @ApiModelProperty(notes = "주최자", example = "xx사업단")
    private String host;
    @ApiModelProperty(notes = "모집 시작날짜", example = "yyyy-mm-dd")
    private LocalDate startDate;
    @ApiModelProperty(notes = "모집 종료날짜", example = "yyyy-mm-dd")
    private LocalDate endDate;
    @ApiModelProperty(notes = "스크랩 수", example = "13")
    private int scrapCount;
    @ApiModelProperty(notes = "댓글 수", example = "8")
    private int commentCount;
}
