package site.chagok.server.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.repository.MemberRepository;
import site.chagok.server.member.service.MemberInfoService;
import site.chagok.server.security.dto.SignUpDto;

@Service
@RequiredArgsConstructor
public class AccountService {

    /*
        사용자 회원가입 및 회원탈퇴 서비스
    */
    private final MemberRepository memberRepository;
    private final MemberInfoService memberInfoService;

    // boolean 유무에 따라 최초 로그인, 회원가입 확인
    @Transactional
    public void signUp(String userEmail, SignUpDto signUpDto) {

        Boolean alreadySaved = memberRepository.findByEmail(userEmail).isPresent();

        // DB에 없으면 회원가입
        if (alreadySaved)
            throw new AuthorizationServiceException("cannot sign up - already Exists Member");
        if (memberInfoService.checkNicknameExists(signUpDto.getNickName()))
            throw new AuthorizationServiceException("cannot sign up - already Exists Nickname");

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
}
