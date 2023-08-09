package site.chagok.server.contest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetContestCommentDto {
    private String memberNickName;
    private String createdDate;
    private String content;
    private Long commentId;
    private Long parentId;
    private boolean deleted;
    private List<GetContestCommentDto> linkedComment;
}
