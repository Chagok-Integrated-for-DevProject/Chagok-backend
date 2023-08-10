package site.chagok.server.study.domain;

import lombok.Getter;
import site.chagok.server.member.domain.Member;
import site.chagok.server.study.domain.Study;

import javax.persistence.*;
@Getter
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
