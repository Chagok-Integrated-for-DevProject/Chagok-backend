package site.chagok.server.contest.domain;


import lombok.*;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;


//commnet,scrap은 글이 삭제된다면 남아있을 필요가 없음
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Contest {
    @Id @GeneratedValue
    private Long id;
    @OneToMany(mappedBy = "contest",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContestScrap> contestScraps = new ArrayList<>();
    @OneToMany(mappedBy = "contest",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String host;
    private String content;
    private String imageUrl;
    private String sourceUrl;
    private int hotCount;
    private int scrapCount;
    private int viewCount;
    private int commentCount;

    public void addViewCount(int viewCount){
        this.viewCount += viewCount;
    }
    public void addCommentCount(){
        this.commentCount++;
    }
}