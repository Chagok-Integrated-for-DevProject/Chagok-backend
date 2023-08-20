package site.chagok.server.contest.util;

import site.chagok.server.contest.domain.Comment;
import site.chagok.server.contest.dto.GetContestCommentDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommentSorter {
    public static Long hasParent =-1L;


    public static List<GetContestCommentDto> getSort(List<Comment> comments) {
        Map<Long,GetContestCommentDto> originalComment = new HashMap<>();
        for(int commentIndex=0;commentIndex<comments.size();commentIndex++){
            Comment comment = comments.get(commentIndex);
            Long parent = comment.getParentId();
            if(parent == hasParent){ //첫번째 댓글이라면
                addParentComment(originalComment, comment);
            } else if(parent != hasParent){ // 대댓글 이라면
                addToParentComment(originalComment, comment, parent);
            }
        }
        return new ArrayList<>(originalComment.values());
    }

    private static void addParentComment(Map<Long, GetContestCommentDto> originalComment, Comment comment) {
        originalComment.put(comment.getId(),GetContestCommentDto.builder()
                .content(comment.getContent())
                .createdDate(comment.getCreatedTime())
                .deleted(comment.isDeleted())
                .memberNickName(comment.getMember().getNickName())
                .commentId(comment.getId())
                .parentId(comment.getParentId())
                .kakaoRef(comment.getKakaoRef())
                .linkedComment(new ArrayList<>())
                .build());
    }

    private static void addToParentComment(Map<Long, GetContestCommentDto> originalComment, Comment comment, Long parent) {
        List<GetContestCommentDto> linkedComment = originalComment.get(parent).getLinkedComment();

        linkedComment.add(GetContestCommentDto.builder()
                .content(comment.getContent())
                .createdDate(comment.getCreatedTime())
                .deleted(comment.isDeleted())
                .memberNickName(comment.getMember().getNickName())
                .commentId(comment.getId())
                .parentId(comment.getParentId())
                .build());
    }
}
