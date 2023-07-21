package site.chagok.server.study.domain;

import site.chagok.server.member.domain.Member;

import javax.persistence.*;

@Entity
public class StudyScrap {

    @Id
    private Long id;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="study_id")
    private Study study;
}
