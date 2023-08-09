package site.chagok.server.project_study.project.domain;

import lombok.*;
import site.chagok.server.project_study.constants.SiteType;
import site.chagok.server.project_study.constants.TechStack;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// Project 게시글 및 Project 게시글 기술 스택 엔티티

@Entity
@Getter
@Setter
@ToString
@Table(name = "project")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Enumerated(EnumType.STRING)
    private SiteType siteType;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<TechStack> techStacks = new ArrayList<>();
}
