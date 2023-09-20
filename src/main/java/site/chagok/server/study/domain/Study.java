package site.chagok.server.study.domain;

import lombok.*;
import site.chagok.server.common.contstans.SiteType;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
    private List<StudyScrap> studyScraps = new ArrayList<>();

    private String title;

    private LocalDateTime createdTime;

    private int viewCount;

    private String sourceUrl;

    private String content;
    private int hotCount;

    private int scrapCount;

    @Enumerated(EnumType.STRING)
    private SiteType siteType;


    @ElementCollection(targetClass = String.class,fetch = FetchType.LAZY)
    @CollectionTable(name = "study_tech_stacks", joinColumns = @JoinColumn(name = "study_id"))
    @Column(name = "tech_stack", nullable = false)
    private List<String> techStacks = new ArrayList<>();;

    public void addViewCount(){
        this.viewCount++;
        this.hotCount++;
    }

    public void addScrapCount(int add){
        this.scrapCount += add;
        this.hotCount -= (add*5);
    }
}
