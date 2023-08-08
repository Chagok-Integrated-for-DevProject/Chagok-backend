package site.chagok.server.contest.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import site.chagok.server.contest.dto.GetContestDto;
import site.chagok.server.contest.service.ContestService;

import javax.management.InstanceNotFoundException;

@Api(tags ="공모전")
@Controller
@RequiredArgsConstructor
public class ContestController {
    private final ContestService contestService;

    @GetMapping(value ="/contests/{id}")
    @ApiOperation(value="공모전 글 상세 조회")
    public GetContestDto getContest(@PathVariable("id") Long id){
        return contestService.getContest(id);
    }


}
