package site.chagok.server.member.domain;


import site.chagok.server.contest.domain.Comment;
import site.chagok.server.contest.domain.ContestScrap;
import site.chagok.server.study.domain.StudyScrap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//scrap은 오로지 유저의 것 -> orphanRemoval = true
//commnet는 맴버가 삭제되어도 남아있어야함
@Entity
public class Member {
    @Id
    private Long id;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<StudyScrap> studyScraps = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ContestScrap> contestScraps = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();
}
