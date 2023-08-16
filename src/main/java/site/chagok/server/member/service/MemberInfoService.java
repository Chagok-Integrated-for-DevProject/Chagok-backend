package site.chagok.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.contest.dto.GetContestPreviewDto;
import site.chagok.server.contest.service.ContestService;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.dto.MemberInfoDto;
import site.chagok.server.member.repository.MemberRepository;
import site.chagok.server.member.util.MemberCredential;
import site.chagok.server.project.dto.GetProjectPreviewDto;
import site.chagok.server.project.service.ProjectService;
import site.chagok.server.study.dto.GetStudyPreviewDto;
import site.chagok.server.study.service.StudyService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberRepository memberRepository;
    private final ContestService contestService;
    private final StudyService studyService;
    private final ProjectService projectService;

    @Transactional
    public MemberInfoDto getMemberInfoDto() {

        String email = MemberCredential.getLoggedMemberEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        // 사용자가 스크랩했던 공모전 글을 미리보기 dto로 반환
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
        return member
                .getContestScraps().stream()
                .map(contest -> contestService.getContestPreview(contest.getContest().getId()))
                .collect(Collectors.toList());
    }

    private List<GetStudyPreviewDto> getStudyPreviewList(Member member) {
        return member
                .getStudyScraps().stream()
                .map(study -> studyService.getStudyPreview(study.getStudy().getId()))
                .collect(Collectors.toList());
    }

    private List<GetProjectPreviewDto> getProjectPreviewDtoList(Member member) {
        return member
                .getProjectScraps().stream()
                .map(project -> projectService.getProjectPreview(project.getProject().getId()))
                .collect(Collectors.toList());
    }

    // 닉네임 있는지 확인
    @Transactional
    public boolean checkNicknameExists(String nickName) {
        return memberRepository.findByNickName(nickName).isPresent();
    }
}
