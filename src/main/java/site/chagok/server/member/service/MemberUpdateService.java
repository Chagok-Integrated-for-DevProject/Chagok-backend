package site.chagok.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.contest.domain.Contest;
import site.chagok.server.contest.domain.ContestScrap;
import site.chagok.server.contest.repository.ContestRepository;
import site.chagok.server.contest.repository.ContestScrapRepository;
import site.chagok.server.member.constants.ActionType;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.dto.BoardScrapDto;
import site.chagok.server.member.exception.NickNameExistsException;
import site.chagok.server.member.repository.MemberRepository;
import site.chagok.server.member.util.MemberCredential;
import site.chagok.server.project.domain.Project;
import site.chagok.server.project.domain.ProjectScrap;
import site.chagok.server.project.repository.ProjectRepository;
import site.chagok.server.project.repository.ProjectScrapRepository;
import site.chagok.server.study.domain.Study;
import site.chagok.server.study.domain.StudyScrap;
import site.chagok.server.study.repository.StudyRepository;
import site.chagok.server.study.repository.StudyScrapRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberUpdateService {

    /*
    사용자 정보 관리
     */

    private final MemberRepository memberRepository;
    private final ContestRepository contestRepository;
    private final ContestScrapRepository contestScrapRepository;
    private final ProjectRepository projectRepository;
    private final ProjectScrapRepository projectScrapRepository;
    private final StudyRepository studyRepository;
    private final StudyScrapRepository studyScrapRepository;
    private final MemberInfoService memberInfoService;

    /*
       이미지 수정, 닉네임 수정, 기술태크 수정, 스크랩
     */

    @Transactional
    public void updateNickName(String nickName) { // 닉네임 변경
        Boolean alreadyUsed = memberInfoService.checkNicknameExists(nickName);

        if (alreadyUsed)
            throw new NickNameExistsException("nickname already exists");

        // 해당 닉네임이 존재하지 않을때,
        String memberEmail = MemberCredential.getLoggedMemberEmail();

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(EntityNotFoundException::new);
        member.updateNickName(nickName);
    }

    private final List<String> categoryList = List.of("study", "project", "contest");

    // 스크랩 추가
    @Transactional
    public void manageBoardScrap(BoardScrapDto boardScrapDto, ActionType action) {

        String category = boardScrapDto.getCategory();
        Long boardId = boardScrapDto.getBoardId();

        if (!categoryList.contains(category)) {
            throw new IllegalStateException();
        }
        
        String userEmail = MemberCredential.getLoggedMemberEmail();
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(EntityExistsException::new);

        switch (category) {
            case "contest" : {
                if (action == ActionType.POST) {
                    Contest scrapContest = contestRepository.findById(boardId).orElseThrow(EntityExistsException::new);
                    ContestScrap contestScrap = new ContestScrap(member, scrapContest);
                    contestScrapRepository.save(contestScrap);
                } else if (action == ActionType.DELETE) {
                    contestScrapRepository.deleteByContestId(boardId).orElseThrow(EntityNotFoundException::new);
                }
                break;
            }
            case "project" : {
                if (action == ActionType.POST) {
                    Project scrapProject = projectRepository.findById(boardId).orElseThrow(EntityExistsException::new);
                    ProjectScrap projectScrap = new ProjectScrap(member, scrapProject);
                    projectScrapRepository.save(projectScrap);
                } else if (action == ActionType.DELETE) {
                    projectScrapRepository.deleteByProjectId(boardId).orElseThrow(EntityNotFoundException::new);
                }
                break;
            }
            case "study" : {
                if (action == ActionType.POST) {
                    Study scrapStudy = studyRepository.findById(boardId).orElseThrow(EntityExistsException::new);
                    StudyScrap studyScrap = new StudyScrap(member, scrapStudy);
                    studyScrapRepository.save(studyScrap);
                } else if (action == ActionType.DELETE) {
                    studyScrapRepository.deleteByStudyId(boardId).orElseThrow(EntityNotFoundException::new);
                }
                break;
            }
        }
    }

    // 기술스택 업데이트
    @Transactional
    public void updateTechStacks(List<String> techStacks) {
        String userEmail = MemberCredential.getLoggedMemberEmail();

        Member member = memberRepository.findByEmail(userEmail).orElseThrow(EntityNotFoundException::new);

        member.updateTechStacks(techStacks);
    }
}
