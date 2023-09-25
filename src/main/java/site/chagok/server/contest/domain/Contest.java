package site.chagok.server.contest.domain;


import lombok.*;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;


//comment,scrap은 글이 삭제된다면 남아있을 필요가 없음
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "contest", cascade = CascadeType.REMOVE)
    private List<ContestScrap> contestScraps = new ArrayList<>();
    @OneToMany(mappedBy = "contest")
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

    public void addViewCount(){
        this.viewCount++;
        this.hotCount++;
    }
    public void addCommentCount(){
        this.commentCount++;
        this.hotCount +=5;
    }
    public void minusCommentCount() {
        this.commentCount--;
        this.hotCount -=5;
    }
    public void addScrapCount(){
        this.scrapCount++;
        this.hotCount +=10;
    }
    public void minusScrapCount() {
        this.scrapCount--;
        this.hotCount -=10;
    }
}