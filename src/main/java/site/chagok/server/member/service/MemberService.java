package site.chagok.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.contest.domain.Contest;
import site.chagok.server.contest.domain.ContestScrap;
import site.chagok.server.contest.repository.ContestRepository;
import site.chagok.server.contest.repository.ContestScrapRepository;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.exception.NickNameExistsException;
import site.chagok.server.member.repository.MemberRepository;
import site.chagok.server.project.domain.Project;
import site.chagok.server.project.domain.ProjectScrap;
import site.chagok.server.project.repository.ProjectRepository;
import site.chagok.server.project.repository.ProjectScrapRepository;
import site.chagok.server.security.dto.ChagokOAuth2User;
import site.chagok.server.study.domain.Study;
import site.chagok.server.study.domain.StudyScrap;
import site.chagok.server.study.repository.StudyRepository;
import site.chagok.server.study.repository.StudyScrapRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    /*
    사용자 회원가입 및 회원탈퇴, 사용자 정보 관리
     */

    private final MemberRepository memberRepository;
    private final ContestRepository contestRepository;
    private final ContestScrapRepository contestScrapRepository;
    private final ProjectRepository projectRepository;
    private final ProjectScrapRepository projectScrapRepository;
    private final StudyRepository studyRepository;
    private final StudyScrapRepository studyScrapRepository;

    @Transactional
    public void signUp(ChagokOAuth2User user) { // 회원가입

        String userEmail = user.getName();

        Boolean alreadySaved = memberRepository.findByEmail(userEmail).isPresent();

        // DB에 없으면 회원가입
        if (!alreadySaved) {
            Member newMember = Member.builder()
                    .email(userEmail)
                    .build();

            memberRepository.save(newMember);
        }
    }

    /*
       이미지 수정, 닉네임 수정, 기술태크 수정, 스크랩
     */

    @Transactional
    public void updateNickName(String nickName) { // 닉네임 변경
        Boolean alreadyUsed = memberRepository.findByEmail(nickName).isPresent();

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
    public void addBoardScrap(String category, Long boardId) {
        
        if (!categoryList.contains(category)) {
            throw new IllegalStateException();
        }
        
        String userEmail = MemberCredential.getLoggedMemberEmail();
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(EntityExistsException::new);

        switch (category) {
            case "contest" : {
                Contest scrapContest = contestRepository.findById(boardId).orElseThrow(EntityExistsException::new);
                ContestScrap contestScrap = new ContestScrap(member, scrapContest);
                contestScrapRepository.save(contestScrap);
                break;
            }
            case "project" : {
                Project scrapProject = projectRepository.findById(boardId).orElseThrow(EntityExistsException::new);
                ProjectScrap projectScrap = new ProjectScrap(member, scrapProject);
                projectScrapRepository.save(projectScrap);
                break;
            }
            case "study" : {
                Study scrapStudy = studyRepository.findById(boardId).orElseThrow(EntityExistsException::new);
                StudyScrap studyScrap = new StudyScrap(member, scrapStudy);
                studyScrapRepository.save(studyScrap);
                break;
            }
        }

    }

}
