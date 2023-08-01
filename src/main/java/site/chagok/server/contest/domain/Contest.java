package site.chagok.server.contest.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


//commnet,scrap은 글이 삭제된다면 남아있을 필요가 ㅇ벗음
@Getter
@Entity
@Builder
@NoArgsConstructor
public class Contest {
    @Id
    private Long id;
    @OneToMany(mappedBy = "contest",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContestScrap> contestScraps = new ArrayList<>();
    @OneToMany(mappedBy = "contest",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String host;
    private String contents;
    private String imageUrl;
    private String originalUrl;
    private int hotCount;
    private int scrapCount;
    private int viewCount;
}