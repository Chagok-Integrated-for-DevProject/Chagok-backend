package site.chagok.server.contest.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//commnet,scrap은 글이 삭제된다면 남아있을 필요가 ㅇ벗음
@Entity
public class Contest {
    @Id
    private Long id;

    @OneToMany(mappedBy = "contest",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContestScrap> contestScraps = new ArrayList<>();
    @OneToMany(mappedBy = "contest",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}