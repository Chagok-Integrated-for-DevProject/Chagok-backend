package site.chagok.server.contest.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.chagok.server.contest.dto.CommentDto;
import site.chagok.server.contest.dto.GetContestCommentDto;
import site.chagok.server.contest.dto.GetContestDto;
import site.chagok.server.contest.service.ContestService;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Api(tags ="공모전")
@RestController
@RequiredArgsConstructor
public class ContestController {
    private final ContestService contestService;

    @GetMapping(value ="/contests/{id}")
    @ApiOperation(value="콘테스트 상세 조회")
    public GetContestDto getContest(@PathVariable("id") Long id){
        return contestService.getContest(id);
    }
    @GetMapping(value="/contests/{id}/comments")
    @ApiOperation(value ="콘테스트 속해 있는 댓글 조회")
    public List<GetContestCommentDto> getContestComment(@PathVariable("id") Long id){
        return contestService.getContestComments(id);
    }

    @PostMapping(value ="/contests/comments")
    @ApiOperation(value ="새로운 댓글 등록",notes = "대댓글이 아니라면 parentId = -1")
    public Long addComment(@RequestBody CommentDto commentDto){
        return contestService.makeComment(commentDto);
    }

    @PostMapping(value="/contests")
    @ApiOperation(value ="새로운 콘테스트 등록(테스트)")
    public void addContest(){
        contestService.makeContest();
    }




}
