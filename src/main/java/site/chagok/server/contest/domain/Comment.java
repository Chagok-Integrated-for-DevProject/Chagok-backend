package site.chagok.server.contest.domain;

import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import site.chagok.server.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Comment
{
    @Id @GeneratedValue
    private Long id;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="contest_id")
    private Contest contest;


    @CreatedDate
    private LocalDate createdDate;
    private String content;
    private Long parentId;
    private boolean deleted;


    @Builder
    public Comment(String content, Long parentId,Contest contest,Member member) {
        this.content = content;
        this.parentId = parentId;
        this.deleted =false;
        this.createdDate = LocalDate.now();
        this.contest = contest;
        this.member = member;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }



}
