package site.chagok.server.study.domain;

import lombok.*;
import site.chagok.server.common.domain.SiteType;
import site.chagok.server.common.domain.TechStack;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// Study 게시글 및 Study 게시글 기술 스택 엔티티
@Entity
@Getter
@Setter
@ToString
@Table(name = "study")
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "study")
    private List<StudyScrap> studyScraps = new ArrayList<>();

    private String title;

    private String nickname;

    private LocalDateTime createdTime;

    private int viewCount;

    private String sourceUrl;

    private String content;
    private int hotCount;

    private int scrapCount;

    @Enumerated(EnumType.STRING)
    private SiteType siteType;

    @ElementCollection(fetch = FetchType.EAGER)
//    @Enumerated(EnumType.STRING)
    private List<String> techStacks = new ArrayList<>();

    public void addViewCount(){
        this.viewCount++;
        this.hotCount++;
    }
    public void addScrapCount(int add){
        this.scrapCount += add;
        this.hotCount -= (add*5);
    }
}
