package site.chagok.server.project_study.project.domain;

import lombok.Getter;
import site.chagok.server.member.domain.Member;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "project_scrap")
public class ProjectScrap {

    @Id
    private Long id;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="project_id")
    private Project project;
}