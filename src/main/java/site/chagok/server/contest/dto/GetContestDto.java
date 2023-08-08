package site.chagok.server.contest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetContestDto  {
    private String title;
    private String imageUrl;
    private String originalUrl;
    private String host;
    private LocalDate startDate;
    private LocalDate endDate;
    private int viewCount;
    private int scrapCount;
}
