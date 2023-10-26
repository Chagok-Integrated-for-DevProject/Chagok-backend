package site.chagok.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.common.exception.BoardNotFoundApiException;
import site.chagok.server.contest.domain.Contest;
import site.chagok.server.contest.domain.ContestScrap;
import site.chagok.server.contest.repository.ContestRepository;
import site.chagok.server.contest.repository.ContestScrapRepository;
import site.chagok.server.member.constants.ActionType;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.dto.BoardScrapDto;
import site.chagok.server.member.exception.ScrapAlreadySavedApiException;
import site.chagok.server.member.exception.ScrapNotFoundApiException;
import site.chagok.server.project.domain.Project;
import site.chagok.server.project.domain.ProjectScrap;
import site.chagok.server.project.repository.ProjectRepository;
import site.chagok.server.project.repository.ProjectScrapRepository;
import site.chagok.server.study.domain.Study;
import site.chagok.server.study.domain.StudyScrap;
import site.chagok.server.study.repository.StudyRepository;
import site.chagok.server.study.repository.StudyScrapRepository;

import java.util.List;
import java.util.Optional;

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

        /*
        카테고리 종류에 따른 스크랩 추가/삭제
        저장 시, 이미 해당 게시글이 스크랩이 된 상태라면, 오류
        삭제 시, 해당 게시글이 스크랩 되지 않았으면, 오류
         */
        switch (category) {
            case "contest" : {
                Contest scrapContest = contestRepository.findById(boardId).orElseThrow(BoardNotFoundApiException::new);

                boolean isSaved = contestScrapRepository.findContestScrapByMemberAndContest(member, scrapContest).isPresent();

                if (action == ActionType.POST) {
                    if (isSaved) {
                        throw new ScrapAlreadySavedApiException();
                    }

                    scrapContest.addScrapCount();
                    ContestScrap contestScrap = new ContestScrap(member, scrapContest);
                    contestScrapRepository.save(contestScrap);
                } else if (action == ActionType.DELETE) {
                    if (!isSaved) {
                        throw new ScrapNotFoundApiException();
                    }

                    scrapContest.minusScrapCount();
                    contestScrapRepository.deleteByContestId(boardId);
                }
                break;
            }
            case "project" : {
                Project scrapProject = projectRepository.findById(boardId).orElseThrow(BoardNotFoundApiException::new);

                boolean isSaved = projectScrapRepository.findProjectScrapByMemberAndProject(member, scrapProject).isPresent();

                if (action == ActionType.POST) {
                    if (isSaved) {
                        throw new ScrapAlreadySavedApiException();
                    }

                    scrapProject.addScrapCount();
                    ProjectScrap projectScrap = new ProjectScrap(member, scrapProject);
                    projectScrapRepository.save(projectScrap);
                } else if (action == ActionType.DELETE) {
                    if (!isSaved) {
                        throw new ScrapNotFoundApiException();
                    }

                    scrapProject.minusScrapCount();
                    projectScrapRepository.deleteByProjectId(boardId).orElseThrow(ScrapNotFoundApiException::new);
                }
                break;
            }
            case "study" : {
                Study scrapStudy = studyRepository.findById(boardId).orElseThrow(BoardNotFoundApiException::new);

                boolean isSaved = studyScrapRepository.findStudyScrapByMemberAndStudy(member, scrapStudy).isPresent();

                if (action == ActionType.POST) {
                    if (isSaved) {
                        throw new ScrapAlreadySavedApiException();
                    }

                    scrapStudy.addScrapCount();
                    StudyScrap studyScrap = new StudyScrap(member, scrapStudy);
                    studyScrapRepository.save(studyScrap);
                } else if (action == ActionType.DELETE) {
                    if (!isSaved) {
                        throw new ScrapNotFoundApiException();
                    }

                    scrapStudy.minusScrapCount();
                    studyScrapRepository.deleteByStudyId(boardId).orElseThrow(ScrapNotFoundApiException::new);
                }
                break;
            }
        }
    }

    // 기술스택 업데이트
    @Transactional
    public void updateTechStacks(List<String> techStacks) {
        Member member = credentialService.getMemberWithTechs();

        member.updateTechStacks(techStacks);
    }
}
