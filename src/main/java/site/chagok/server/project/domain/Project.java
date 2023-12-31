package site.chagok.server.project.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import site.chagok.server.common.contstans.SiteType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter

@ToString
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<ProjectScrap> projectScraps = new ArrayList<>();

    private String title;
    private LocalDateTime createdTime;
    private int viewCount;
    private String sourceUrl;
    private String content;
    private int scrapCount;
    private int hotCount;

    @Enumerated(EnumType.STRING)
    private SiteType siteType;


    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "project_tech_stacks", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "tech_stacks", nullable = false)
    private Set<String> techStacks = new HashSet<>();

    public void addViewCount(){
        this.viewCount++;
        this.hotCount++;
    }
    public void addScrapCount() {
        this.scrapCount++;
        this.hotCount += 10;
    }
    public void minusScrapCount() {
        this.scrapCount--;
        this.hotCount -= 10;
    }
}
