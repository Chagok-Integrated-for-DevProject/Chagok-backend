package site.chagok.server.member.dto;

import lombok.*;
import site.chagok.server.common.contstans.contstans.SocialType;
import site.chagok.server.contest.dto.GetContestPreviewDto;
import site.chagok.server.project.dto.GetProjectPreviewDto;
import site.chagok.server.study.dto.GetStudyPreviewDto;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDto {

    private String nickName;
    private String profileImg;
    private SocialType social;
    private String email;
    private List<String> techStacks;
    private List<GetContestPreviewDto> contestScraps;
    private List<GetStudyPreviewDto> studyScraps;
    private List<GetProjectPreviewDto> projectScraps;
}
