package site.chagok.server.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chagok.server.common.domain.SiteType;
import site.chagok.server.common.domain.TechStack;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class GetStudyPreviewDto {
    private String title;
    private SiteType siteType;
    private List<TechStack> techStacks;
    private int viewCount;
    private int scrapCount;
    private String preview;
}
