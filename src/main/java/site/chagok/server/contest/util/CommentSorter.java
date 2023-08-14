package site.chagok.server.contest.util;

import site.chagok.server.contest.domain.Comment;
import site.chagok.server.contest.dto.GetContestCommentDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommentSorter {
    public static Long ORIGINAL_COMMENT_PARENT_ID =-1L;


    public static List<GetContestCommentDto> getSort(List<Comment> comments) {
        Map<Long,GetContestCommentDto> originalComment = new HashMap<>();
        for(int commentIndex=0;commentIndex<comments.size();commentIndex++){
            Comment comment = comments.get(commentIndex);
            Long parent = comment.getParentId();
            if(parent ==ORIGINAL_COMMENT_PARENT_ID){ //첫번쨰 댓글이라면
                addParentComment(originalComment, comment);
            }
            if(parent != ORIGINAL_COMMENT_PARENT_ID){
                addToParentComment(originalComment, comment, parent);
            }
        }
        return originalComment.values().stream().collect(Collectors.toList());
    }

    private static void addParentComment(Map<Long, GetContestCommentDto> originalComment, Comment comment) {
        originalComment.put(comment.getId(),GetContestCommentDto.builder()
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate().toString())
                .deleted(comment.isDeleted())
                .memberNickName(comment.getMember().getNickName())
                .commentId(comment.getId())
                .parentId(comment.getParentId())
                .build());
    }

    private static void addToParentComment(Map<Long, GetContestCommentDto> originalComment, Comment comment, Long parent) {
        List<GetContestCommentDto> linkedComment = originalComment.get(parent).getLinkedComment();
        if(linkedComment==null) linkedComment = new ArrayList<>();
        linkedComment.add(GetContestCommentDto.builder()
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate().toString())
                .deleted(comment.isDeleted())
                .memberNickName(comment.getMember().getNickName())
                .commentId(comment.getId())
                .parentId(comment.getParentId())
                .build());
    }
}
