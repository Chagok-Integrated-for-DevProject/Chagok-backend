package site.chagok.server.project.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import site.chagok.server.common.domain.SiteType;
import site.chagok.server.common.domain.TechStack;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;




@Entity
@Getter
@Setter

@ToString
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String nickname;
    private LocalDateTime createdTime;
    private int viewCount;
    private String sourceUrl;
    private String content;
    private int scrapCount;

    @Enumerated(EnumType.STRING)
    private SiteType siteType;


    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "project_tech_stacks", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "tech_stacks", nullable = false)
    private List<String> techStacks = new ArrayList<>();

//    @ElementCollection
//    @Enumerated(EnumType.STRING)
//    private List<TechStack> techStacks = new ArrayList<>();

}
