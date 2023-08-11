package site.chagok.server.study.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chagok.server.member.domain.Member;
import site.chagok.server.study.domain.Study;

import javax.persistence.*;
@Getter
@Entity
@NoArgsConstructor
public class StudyScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch= FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch= FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="study_id")
    private Study study;

    public StudyScrap(Member member, Study study) {
        this.member = member;
        this.study = study;
    }

}
