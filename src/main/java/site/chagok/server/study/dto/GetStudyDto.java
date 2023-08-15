package site.chagok.server.study.dto;

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
public class GetStudyDto {
    private String title;
    private String nickName;
    private String content;
    private String sourceUrl;
    private LocalDateTime createdTime;
    private int scrapCount;
    private int viewCount;
    private SiteType siteType;
    private List<String> techStacks;
}
