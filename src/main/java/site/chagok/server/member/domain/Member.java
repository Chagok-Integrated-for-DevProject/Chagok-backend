package site.chagok.server.member.domain;


import lombok.*;
import site.chagok.server.common.contstans.SocialType;
import site.chagok.server.common.domain.BaseTime;
import site.chagok.server.contest.domain.Comment;
import site.chagok.server.contest.domain.ContestScrap;
import site.chagok.server.project.domain.ProjectScrap;
import site.chagok.server.study.domain.StudyScrap;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//scrap은 오로지 유저의 것 -> orphanRemoval = true
//commnet는 맴버가 삭제되어도 남아있어야함
@Getter
@Entity
@Setter
@NoArgsConstructor
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<StudyScrap> studyScraps = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ContestScrap> contestScraps = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ProjectScrap> projectScraps = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();


    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "member_tech_stacks", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "tech_stack", nullable = false)
    private Set<String> techStacks = new HashSet<>();

    private String nickName;
    private String email;
    private String profileImg;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Builder
    public Member(String nickName, String email, String profileImg, SocialType socialType, Set<String> techStacks) {
        this.nickName = nickName;
        this.email = email;
        this.profileImg = profileImg;
        this.socialType = socialType;
        this.techStacks = techStacks;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void updateTechStacks(List<String> techStacks) {
        // 초기화 하고, 추가할 데이터 업데이트
        this.techStacks.clear();
        this.techStacks.addAll(techStacks);
    }
}
