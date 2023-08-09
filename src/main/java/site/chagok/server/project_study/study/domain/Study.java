package site.chagok.server.project_study.study.domain;

import lombok.Getter;
import lombok.Setter;
import site.chagok.server.project_study.constants.SiteType;
import site.chagok.server.project_study.constants.TechStack;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Enumerated(EnumType.STRING)
    private SiteType siteType;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<TechStack> techStacks = new ArrayList<>();

}
