package site.chagok.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.common.exception.BoardNotFoundException;
import site.chagok.server.contest.domain.Contest;
import site.chagok.server.contest.domain.ContestScrap;
import site.chagok.server.contest.repository.ContestRepository;
import site.chagok.server.contest.repository.ContestScrapRepository;
import site.chagok.server.member.constants.ActionType;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.dto.BoardScrapDto;
import site.chagok.server.member.exception.NickNameExistsException;
import site.chagok.server.member.exception.ScrapNotFoundException;
import site.chagok.server.member.repository.MemberRepository;
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
    멤버관리 - 사용자 정보 관리 서비스
     */

    private final ContestRepository contestRepository;
    private final ContestScrapRepository contestScrapRepository;
    private final ProjectRepository projectRepository;
    private final ProjectScrapRepository projectScrapRepository;
    private final StudyRepository studyRepository;
    private final StudyScrapRepository studyScrapRepository;
    private final MemberInfoService memberInfoService;
    private final MemberCredentialService credentialService;


    /*
       이미지 수정, 닉네임 수정, 기술태크 수정, 스크랩
     */

    @Transactional
    public void updateNickName(String nickName) { // 닉네임 변경
        memberInfoService.checkNicknameExists(nickName);

        Member member = credentialService.getMember();
        member.updateNickName(nickName);
    }

    private final List<String> categoryList = List.of("study", "project", "contest");

    // 스크랩 추가
    @Transactional
    public void manageBoardScrap(BoardScrapDto boardScrapDto, ActionType action) {

        String category = boardScrapDto.getCategory();
        Long boardId = boardScrapDto.getBoardId();

        if (!categoryList.contains(category)) {
            throw new IllegalStateException("invalid category type");
        }

        Member member = credentialService.getMember();

        // 카테고리 종류에 따른 스크랩 추가/삭제
        switch (category) {
            case "contest" : {
                Contest scrapContest = contestRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
                if (action == ActionType.POST) {
                    scrapContest.addScrapCount();
                    ContestScrap contestScrap = new ContestScrap(member, scrapContest);
                    contestScrapRepository.save(contestScrap);
                } else if (action == ActionType.DELETE) {
                    scrapContest.minusScrapCount();
                    contestScrapRepository.deleteByContestId(boardId).orElseThrow(ScrapNotFoundException::new);
                }
                break;
            }
            case "project" : {
                Project scrapProject = projectRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
                if (action == ActionType.POST) {
                    scrapProject.addScrapCount();
                    ProjectScrap projectScrap = new ProjectScrap(member, scrapProject);
                    projectScrapRepository.save(projectScrap);
                } else if (action == ActionType.DELETE) {
                    scrapProject.minusScrapCount();
                    projectScrapRepository.deleteByProjectId(boardId).orElseThrow(ScrapNotFoundException::new);
                }
                break;
            }
            case "study" : {
                Study scrapStudy = studyRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
                if (action == ActionType.POST) {
                    scrapStudy.addScrapCount();
                    StudyScrap studyScrap = new StudyScrap(member, scrapStudy);
                    studyScrapRepository.save(studyScrap);
                } else if (action == ActionType.DELETE) {
                    scrapStudy.minusScrapCount();
                    studyScrapRepository.deleteByStudyId(boardId).orElseThrow(ScrapNotFoundException::new);
                }
                break;
            }
        }
    }

    // 기술스택 업데이트
    @Transactional
    public void updateTechStacks(List<String> techStacks) {
        Member member = credentialService.getMember();

        member.updateTechStacks(techStacks);
    }
}
