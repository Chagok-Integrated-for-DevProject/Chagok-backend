package site.chagok.server.contest.domain;

import lombok.Getter;
import site.chagok.server.member.domain.Member;

import javax.persistence.*;
@Getter
@Entity
public class ContestScrap {
    @Id
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="contest_id")
    private Contest contest;
}
