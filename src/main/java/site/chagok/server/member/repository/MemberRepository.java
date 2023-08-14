package site.chagok.server.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.chagok.server.member.domain.Member;

import java.awt.*;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickName(String nickName);
    Optional<ProfileImageMapping> findByProfileImg(String profileImg);
}
