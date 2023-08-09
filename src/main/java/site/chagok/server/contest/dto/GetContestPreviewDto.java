package site.chagok.server.contest.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetContestPreviewDto {
    private Long contestId;
    private String title;
    private String imageUrl;
    private String host;
    private String startDate;
    private String endDate;
    private int scrapCount;
    private int commentCount;
}
