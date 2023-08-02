package site.chagok.server.contest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chagok.server.contest.domain.Contest;

import java.time.LocalDate;
import java.util.function.Function;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetContestDto  {
    private String title;
    private String imageUrl;
    private String originalUrl;
    private String host;
    private String startDate;
    private String endDate;
    private int viewCount;
    private int scrapCount;
}
