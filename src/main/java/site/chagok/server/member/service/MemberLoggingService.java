package site.chagok.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.common.contstans.SocialType;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberLoggingService {

    /*
    사용자 회원가입 및 회원탈퇴 서비스
     */
    private final MemberRepository memberRepository;

    // boolean 유무에 따라 최초 로그인, 회원가입 확인
    @Transactional
    public boolean signUp(String userEmail, SocialType socialType) {

        Boolean alreadySaved = memberRepository.findByEmail(userEmail).isPresent();

        // DB에 없으면 회원가입
        if (!alreadySaved) {
            Member newMember = Member.builder()
                    .email(userEmail)
                    .socialType(socialType)
                    .build();

            memberRepository.save(newMember);

            return true;
        }

        return false;
    }
}
