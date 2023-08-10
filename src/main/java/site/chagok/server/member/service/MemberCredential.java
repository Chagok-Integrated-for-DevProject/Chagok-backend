package site.chagok.server.member.service;

import org.springframework.security.core.context.SecurityContextHolder;

public class MemberCredential {

    // 유틸리티

    // security context로부터 현재 로그인한 사용자 email return함수
    public static String getLoggedMemberEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
