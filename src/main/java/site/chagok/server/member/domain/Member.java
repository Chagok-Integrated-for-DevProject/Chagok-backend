package site.chagok.server.member.domain;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import site.chagok.server.contest.domain.Comment;
import site.chagok.server.contest.domain.ContestScrap;
import site.chagok.server.study.domain.StudyScrap;
import site.chagok.server.common.domain.TechStack;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//scrap은 오로지 유저의 것 -> orphanRemoval = true
//commnet는 맴버가 삭제되어도 남아있어야함
@Getter
@Entity
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyScrap> studyScraps = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContestScrap> contestScraps = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<TechStack> techStacks = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdTime;

    private String nickName;
    private String email;
    private String profileImg;

    @Builder
    public Member(String nickName, String email, String profileImg) {
        this.nickName = nickName;
        this.email = email;
        this.profileImg = profileImg;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void addContestScrap(ContestScrap contestScrap) {
        contestScraps.add(contestScrap);
    }
}
