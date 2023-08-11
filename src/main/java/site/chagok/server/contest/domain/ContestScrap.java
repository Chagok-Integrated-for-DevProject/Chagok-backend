package site.chagok.server.contest.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chagok.server.member.domain.Member;

import javax.persistence.*;
@Getter
@Entity
@NoArgsConstructor
public class ContestScrap {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name ="contest_id")
    private Contest contest;

    public ContestScrap(Member member, Contest contest) {
        this.member = member;
        this.contest = contest;
    }
}
