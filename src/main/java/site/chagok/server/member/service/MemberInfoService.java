package site.chagok.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.common.contstans.PostType;
import site.chagok.server.contest.domain.Contest;
import site.chagok.server.contest.dto.GetContestPreviewDto;
import site.chagok.server.contest.repository.ContestScrapRepository;
import site.chagok.server.contest.service.ContestService;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.dto.MemberInfoDto;
import site.chagok.server.member.exception.NickNameExistsApiException;
import site.chagok.server.member.repository.MemberRepository;
import site.chagok.server.project.domain.Project;
import site.chagok.server.project.dto.GetProjectPreviewDto;
import site.chagok.server.project.repository.ProjectScrapRepository;
import site.chagok.server.project.service.ProjectService;
import site.chagok.server.study.domain.Study;
import site.chagok.server.study.dto.GetStudyPreviewDto;
import site.chagok.server.study.repository.StudyScrapRepository;
import site.chagok.server.study.service.StudyService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    /*
    멤버관리 - 사용자 정보 제공 서비스
     */
    private final MemberRepository memberRepository;
    private final ContestService contestService;
    private final StudyService studyService;
    private final ProjectService projectService;
    private final MemberCredentialService credentialService;

    private final ProjectScrapRepository projectScrapRepository;
    private final StudyScrapRepository studyScrapRepository;
    private final ContestScrapRepository contestScrapRepository;

    @Transactional
    public MemberInfoDto getMemberInfoDto() {

        Member member = credentialService.getMemberWithTechs();

        // 사용자가 스크랩 했던 공모전 글을 미리보기 dto로 반환
        List<GetContestPreviewDto> contestScraps = getContestPreviewList(member);
        List<GetStudyPreviewDto> studyScraps = getStudyPreviewList(member);
        List<GetProjectPreviewDto> projectScraps = getProjectPreviewDtoList(member);

        return MemberInfoDto.builder()
                .nickName(member.getNickName())
                .email(member.getEmail())
                .social(member.getSocialType())
                .techStacks(member.getTechStacks())
                .profileImg(member.getProfileImg())
                .contestScraps(contestScraps)
                .studyScraps(studyScraps)
                .projectScraps(projectScraps)
                .build();
    }

    private List<GetContestPreviewDto> getContestPreviewList(Member member) {
        List<Contest> scrapedContests = contestScrapRepository.findContestByMember(member);

        return contestService.getContestPreview(scrapedContests);
    }

    private List<GetStudyPreviewDto> getStudyPreviewList(Member member) {

        List<Study> scrapedStudies = studyScrapRepository.findStudyByMemberWithTechs(member);

        return studyService.getStudyPreview(scrapedStudies);
    }

    private List<GetProjectPreviewDto> getProjectPreviewDtoList(Member member) {

        List<Project> scrapedProjects = projectScrapRepository.findProjectByMemberWithTechs(member);

        return projectService.getProjectPreview(scrapedProjects);
    }

    // 닉네임 있는지 확인
    @Transactional(readOnly = true)
    public void checkNicknameExists(String nickName) {

        if (memberRepository.findByNickName(nickName).isPresent()) { // 이미 존재한다면, exception 처리
            throw new NickNameExistsApiException();
        }

    }
}
