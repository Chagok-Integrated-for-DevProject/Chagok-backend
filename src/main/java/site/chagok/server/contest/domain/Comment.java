package site.chagok.server.contest.domain;

import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import site.chagok.server.common.domain.BaseTime;
import site.chagok.server.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends BaseTime {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="contest_id")
    private Contest contest;

    // 댓글내용
    private String content;

    // 카카오톡 연락주소
    private String kakaoRef;

    // 부모댓글 id 없으면 -1
    private Long parentId;
    private boolean deleted;


    @Builder
    public Comment(String content, Long parentId, Contest contest,Member member, String kakaoRef) {
        this.content = content;
        this.parentId = parentId;
        this.deleted = false;
        this.contest = contest;
        this.member = member;
        this.kakaoRef = kakaoRef;
    }

    public void setDeleted() {
        this.deleted = true;
    }

    public void setDeletedNoMember() {
        this.deleted = true;
        this.member = null;
    }

    public void updateComment(String content, String kakaoRef) {
        this.content = content;
        this.kakaoRef = kakaoRef;
    }
}
