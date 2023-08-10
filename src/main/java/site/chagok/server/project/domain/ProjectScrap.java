package site.chagok.server.project.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chagok.server.member.domain.Member;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class ProjectScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="project_id")
    private Project project;

    public ProjectScrap(Member member, Project project) {
        this.member = member;
        this.project = project;
    }
}
