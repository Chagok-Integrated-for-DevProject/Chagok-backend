package site.chagok.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.repository.MemberRepository;
import site.chagok.server.security.dto.ChagokOAuth2User;

@Service
@RequiredArgsConstructor
public class MemberLoggingService {

    /*
    사용자 회원가입 및 회원탈퇴 서비스
     */
    private final MemberRepository memberRepository;

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
}
