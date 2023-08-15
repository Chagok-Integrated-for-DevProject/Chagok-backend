package site.chagok.server.contest.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.chagok.server.contest.dto.CommentDto;
import site.chagok.server.contest.dto.GetContestCommentDto;
import site.chagok.server.contest.dto.GetContestDto;
import site.chagok.server.contest.dto.GetContestPreviewDto;
import site.chagok.server.contest.service.ContestService;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Api(tags ="공모전")
@RestController
@RequiredArgsConstructor
public class ContestController {
    private final ContestService contestService;
    private static final String CONTEST_DEFAULT_SIZE ="3";

    private static final String CONTEST_DEFAULT_SORT ="hotCount";

    @GetMapping(value ="/contests/{id}")
    @ApiOperation(value="공모전 글 상세 조회")
    public GetContestDto getContest(@PathVariable("id") Long id){
        return contestService.getContest(id);
    }
    @GetMapping(value="/contests/{id}/comments")
    @ApiOperation(value ="콘테스트 속해 있는 댓글 조회",notes=" 대댓글은 Linked comment")
    public List<GetContestCommentDto> getContestComment(@PathVariable("id") Long id){
        return contestService.getContestComments(id);
    }

    @PostMapping(value ="/contests/comments")
    @ApiOperation(value ="secure - 새로운 댓글 등록",notes = "대댓글이 아니라면 parentId = -1")
    public Long addComment(@RequestBody CommentDto commentDto){
        return contestService.makeComment(commentDto);
    }

    @PostMapping(value="/contests")
    @ApiOperation(value ="새로운 콘테스트 등록(테스트)")
    public void addContest() {
        contestService.makeContest();
    }

    @GetMapping(value="/contests")
    @ApiOperation(value = "콘테스트 정렬" ,notes = "page(기본값 0),size(기본값 3),sort(기본값 hotCount),direction(기본값 desc) / 마감순은 id,desc")
    public Page<GetContestPreviewDto> getContests(
            @RequestParam(value ="size",required = false,defaultValue = CONTEST_DEFAULT_SIZE) int size
            ,@RequestParam(value="page",required = false,defaultValue = "0") int page
            ,@RequestParam(value = "sort",required = false,defaultValue =CONTEST_DEFAULT_SORT) String sort
            ,@RequestParam(value="direction",required = false,defaultValue = "desc")String direction) {
        if(direction.equals("desc")){
            return contestService.getContests(PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,sort)));
        }
        return contestService.getContests(PageRequest.of(page,size,Sort.by(Sort.Direction.ASC,sort)));
    }



}
