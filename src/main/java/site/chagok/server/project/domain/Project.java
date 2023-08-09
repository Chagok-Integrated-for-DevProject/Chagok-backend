package site.chagok.server.project.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// Project 게시글 및 Project 게시글 기술 스택 엔티티

@Entity
@Getter
@Setter
@ToString
@Table(name = "study")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String nickname;

    private LocalDateTime createdTime;

    private int viewCount;

    private String sourceUrl;

    private String content;

    private int scrapCount;

    private String siteType;

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "project_tech_stacks", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "tech", nullable = false)
    private List<String> techStacks = new ArrayList<>();
}
