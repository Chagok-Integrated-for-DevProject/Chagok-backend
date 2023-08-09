package site.chagok.server.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.chagok.server.member.domain.Member;

import java.awt.*;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
