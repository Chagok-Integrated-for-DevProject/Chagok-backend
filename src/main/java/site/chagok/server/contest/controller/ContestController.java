package site.chagok.server.contest.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extensions;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.chagok.server.common.dto.ErrorDto;
import site.chagok.server.contest.dto.*;
import site.chagok.server.contest.service.ContestService;
import springfox.documentation.service.Response;

import javax.management.InstanceNotFoundException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "공모전 API")
public class ContestController {

    private final ContestService contestService;
    private static final String CONTEST_DEFAULT_SIZE ="3";

    private static final String CONTEST_DEFAULT_SORT ="hotCount";

    @GetMapping(value ="/contests/{id}")
    @ApiOperation(value="공모전 글 상세 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "공모전 게시글 조회 성공"),
            @ApiResponse(code = 404, message = "board_01 - 게시글 조회 오류", response = ErrorDto.class)
    })
    public GetContestDto getContest(@PathVariable("id") Long id){
        return contestService.getContest(id);
    }

    @GetMapping(value="/contests")
    @ApiOperation(value = "콘테스트 정렬" , notes = "page(기본값 0),size(기본값 3),sort(기본값 hotCount),direction(기본값 desc) / 마감순은 id,desc")
    public Page<GetContestPreviewDto> getContests(
            @RequestParam(value ="size",required = false,defaultValue = CONTEST_DEFAULT_SIZE) int size
            ,@RequestParam(value="page",required = false,defaultValue = "0") int page
            ,@RequestParam(value = "sort",required = false,defaultValue = CONTEST_DEFAULT_SORT) String sort
            ,@RequestParam(value="direction",required = false,defaultValue = "desc")String direction) {
        if(direction.equals("desc")){
            return contestService.getContests(PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,sort)));
        }
        return contestService.getContests(PageRequest.of(page,size,Sort.by(Sort.Direction.ASC,sort)));
    }

    @GetMapping(value="/contests/{id}/comments")
    @ApiOperation(value ="콘테스트 속해 있는 댓글 조회",notes=" 대댓글은 Linked comment")
    @ApiResponses({
            @ApiResponse(code = 200, message = "댓글 조회 성공"),
            @ApiResponse(code = 404, message = "board_01 - 게시글 조회 오류(댓글 못찾음)", response = ErrorDto.class)
    })
    public List<GetContestCommentDto> getContestComment(@PathVariable("id") Long id){
        return contestService.getContestComments(id);
    }

    @PostMapping(value ="/contests/comments")
    @ApiOperation(value ="secure - 새로운 댓글 등록", notes = "대댓글이 아니라면 parentId = -1")
    @ApiResponses({
            @ApiResponse(code = 200, message = "댓글 등록 성공"),
            @ApiResponse(code = 404, message = "board_01 - 게시글 조회 오류(댓글 못찾음)")
    })
    public Long addComment(@RequestBody CommentDto commentDto){
        return contestService.makeComment(commentDto);
    }

    @PutMapping(value = "/contests/comments")
    @ApiOperation(value ="secure - 댓글 수정")
    @ApiResponses({
            @ApiResponse(code = 200, message = "댓글 수정 성공"),
            @ApiResponse(code = 404, message = "comment_01 - 댓글 조회 오류", response = ErrorDto.class)
    })
    public ResponseEntity updateComment(@RequestBody CommentUpdateDto commentUpdateDto) {
        contestService.updateComment(commentUpdateDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/contests/comments/{commentId}")
    @ApiOperation(value ="secure - 댓글 삭제", notes = "삭제할 댓글 id url 파라미터")
    @ApiResponses({
            @ApiResponse(code = 200, message = "댓글 삭제 성공"),
            @ApiResponse(code = 404, message = "comment_01 - 댓글 조회 오류", response = ErrorDto.class)
    })
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId) {
        contestService.deleteComment(commentId);

        return new ResponseEntity(HttpStatus.OK);
    }
}
