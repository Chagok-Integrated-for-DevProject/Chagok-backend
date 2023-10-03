package site.chagok.server.contest.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chagok.server.common.dto.ErrorDto;
import site.chagok.server.contest.dto.*;
import site.chagok.server.contest.service.ContestService;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "공모전 API")
public class ContestController {

    private final ContestService contestService;
    private static final String CONTEST_DEFAULT_SIZE ="3";

    private static final String CONTEST_DEFAULT_SORT ="hotCount";

    @GetMapping(value ="/contests/{id}")
    @Operation(summary="공모전 글 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공모전 게시글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "board_01 - 게시글 조회 오류", content = @Content(
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    public GetContestDto getContest(@PathVariable("id") Long id){
        return contestService.getContest(id);
    }

    @GetMapping(value="/contests")
    @Operation(summary = "콘테스트 정렬" , description = "page(기본값 0),size(기본값 3),sort(기본값 hotCount),direction(기본값 desc) / 마감순은 id,desc")
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
    @Operation(summary ="콘테스트 속해 있는 댓글 조회",description=" 대댓글은 Linked comment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "board_01 - 게시글 조회 오류(댓글 못찾음)")
    })
    public List<GetContestCommentDto> getContestComment(@PathVariable("id") Long id){
        return contestService.getContestComments(id);
    }

    @PostMapping(value ="/contests/comments")
    @Operation(summary ="secure - 새로운 댓글 등록", description = "대댓글이 아니라면 parentId = -1")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 등록 성공"),
            @ApiResponse(responseCode = "404", description = "board_01 - 게시글 조회 오류(댓글 못찾음)", content = @Content(
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    public Long addComment(@RequestBody CommentDto commentDto){
        return contestService.makeComment(commentDto);
    }

    @PutMapping(value = "/contests/comments")
    @Operation(summary ="secure - 댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "404", description = "comment_01 - 댓글 조회 오류", content = @Content(
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    public ResponseEntity updateComment(@RequestBody CommentUpdateDto commentUpdateDto) {
        contestService.updateComment(commentUpdateDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/contests/comments/{commentId}")
    @Operation(summary ="secure - 댓글 삭제", description = "삭제할 댓글 id url 파라미터")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "comment_01 - 댓글 조회 오류", content = @Content(
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId) {
        contestService.deleteComment(commentId);

        return new ResponseEntity(HttpStatus.OK);
    }
}
