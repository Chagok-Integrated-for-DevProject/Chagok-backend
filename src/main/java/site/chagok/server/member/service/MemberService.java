package site.chagok.server.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.repository.MemberRepository;
import site.chagok.server.security.dto.ChagokOAuth2User;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Transactional
    public void signUp(ChagokOAuth2User user) {

        String userEmail = user.getName();

        Optional<Member> savedMember = memberRepository.findByEmail(userEmail);

        if (savedMember.isEmpty()) {
            Member newMember = Member.builder()
                    .email(userEmail)
                    .build();

            memberRepository.save(newMember);
        }
    }

}
