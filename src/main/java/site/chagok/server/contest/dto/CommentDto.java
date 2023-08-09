package site.chagok.server.contest.dto;


import lombok.Getter;

@Getter
public class CommentDto {
    private String content;
    private Long parentId;
    private Long contestId;
}
