package site.chagok.server.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.contest.domain.Comment;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.exception.UpdateInfoException;
import site.chagok.server.member.repository.MemberRepository;
import site.chagok.server.member.service.MemberCredentialService;
import site.chagok.server.member.service.MemberImgService;
import site.chagok.server.member.service.MemberInfoService;
import site.chagok.server.security.dto.SignUpDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    /*
        사용자 회원가입 및 회원탈퇴 서비스
    */
    private final MemberRepository memberRepository;
    private final MemberInfoService memberInfoService;
    private final MemberImgService memberImgService;
    private final MemberCredentialService credentialService;

    // boolean 유무에 따라 최초 로그인, 회원가입 확인
    @Transactional
    public void signUp(String userEmail, SignUpDto signUpDto) {

        Boolean alreadySaved = memberRepository.findByEmail(userEmail).isPresent();

        // DB에 없으면 회원가입
        if (alreadySaved)
            throw new UpdateInfoException("member_01", "cannot sign up - already Exists Member");

        memberInfoService.checkNicknameExists(signUpDto.getNickName());

        Member member = Member.builder()
                .email(userEmail)
                .nickName(signUpDto.getNickName())
                .socialType(signUpDto.getSocialType())
                .techStacks(signUpDto.getTechStacks())
                .build();

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public boolean isSignUp(String userEmail) {
        return memberRepository.findByEmail(userEmail).isPresent();
    }

    @Transactional
    public void deleteAccount() {

        Member member = credentialService.getMember();

        deleteInfo(member);

        // member 삭제
        memberRepository.delete(member);
    }

    private void deleteInfo(Member member) { // 사용자 정보 삭제

        List<Comment> comments = member.getComments();

        // 댓글 삭제처리
        comments.forEach(Comment::setDeletedNoMember);

        // 프로필 이미지 삭제처리
        memberImgService.deleteProfileImg();

        // 스크랩은 cascade에 따라 삭제 처리.
    }
}
