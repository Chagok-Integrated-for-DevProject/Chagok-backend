package site.chagok.server.project.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.chagok.server.common.dto.ErrorDto;
import site.chagok.server.project.dto.GetProjectDto;
import site.chagok.server.project.dto.GetProjectPreviewDto;
import site.chagok.server.project.dto.GetRecommendedProjectDto;
import site.chagok.server.project.service.ProjectService;
import site.chagok.server.study.dto.GetRecommendedStudyDto;
import site.chagok.server.study.dto.GetStudyDto;
import site.chagok.server.study.dto.GetStudyPreviewDto;

import java.util.List;

@Tag(name = "프로젝트 API")
@RequiredArgsConstructor
@RestController
public class ProjectController {
    private final ProjectService projectService;
    private static final String PROJECT_DEFAULT_SIZE ="3";
    private static final String PROJECT_DEFAULT_SORT ="hotCount";
    @GetMapping(value ="/projects")
    @ApiOperation(value = "프로젝트 정렬",notes = "파라미터 techStacks(스택 리스트),searchTerm(검색어),pageNumber(기본값 0),pageSize(기본값 3),sort(기본값 hotCount,desc / 마감순은 id,desc)")
    public Page<GetProjectPreviewDto> getProjects(
            @RequestParam(value ="searchTerm",required = false)String searchTerm
            ,@RequestParam(value="techStacks",required = false) List<String> techStacks
            ,@RequestParam(value ="size",required = false,defaultValue = PROJECT_DEFAULT_SIZE) int size
            ,@RequestParam(value="page",required = false,defaultValue = "0") int page
            ,@RequestParam(value = "sort",required = false,defaultValue =PROJECT_DEFAULT_SORT) String sort
            ,@RequestParam(value="direction",required = false,defaultValue = "desc")String direction){
        if(direction.equals("desc")){
            return projectService.getProjects(searchTerm,techStacks, PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,sort)));
        }
        return projectService.getProjects(searchTerm,techStacks, PageRequest.of(page,size,Sort.by(Sort.Direction.ASC,sort)));
    }
    @GetMapping (value="/projects/{id}")
    @ApiOperation(value = "프로젝트 상세보기")
    @ApiResponses({
            @ApiResponse(code = 200, message = "프로젝트 게시글 조회 성공"),
            @ApiResponse(code = 404, message = "board_01 - 게시글 조회 오류", response = ErrorDto.class)
    })
    public GetProjectDto getProject(@PathVariable("id") Long projectId){
        return projectService.getProject(projectId);
    }

    @GetMapping (value="/projects/recommend")
    @ApiOperation(value = "스터디 추천 받기")
    public List<GetRecommendedProjectDto> getRecommendedProject(){
        return projectService.getRecommendedProject();
    }
}
