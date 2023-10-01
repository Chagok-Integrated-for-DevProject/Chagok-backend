package site.chagok.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.exception.MemberNotFoundApiException;
import site.chagok.server.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberCredentialService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member getMember() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        return memberRepository.findByEmail(userEmail).orElseThrow(MemberNotFoundApiException::new);
    }
}
