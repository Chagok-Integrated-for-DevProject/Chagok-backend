package site.chagok.server.study.domain;

import lombok.*;

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

    private int scrapCount;

    private String siteType;

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "study_tech_stacks", joinColumns = @JoinColumn(name = "study_id"))
    @Column(name = "tech", nullable = false)
    private List<String> techStacks = new ArrayList<>();

}
