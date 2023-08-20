package site.chagok.server.study.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import site.chagok.server.study.dto.GetRecommendedStudyDto;
import site.chagok.server.study.dto.GetStudyDto;
import site.chagok.server.study.dto.GetStudyPreviewDto;
import site.chagok.server.study.service.StudyService;

import java.util.List;



@Api(tags = "스터디")
@RequiredArgsConstructor
@RestController
public class StudyController {
    private final StudyService studyService;
    private static final String STUDY_DEFAULT_SIZE ="3";
    private static final String STUDY_DEFAULT_SORT ="hotCount";
    @GetMapping(value="/studies")
    @ApiOperation(value = "스터디 정렬",notes = "파라미터 techStacks(스택 리스트),searchTerm(검색어),pageNumber(기본값 0),pageSize(기본값 3),sort(기본값 hotCount,desc / 마감순은 id,desc)")
    public Page<GetStudyPreviewDto> getStudies(
             @RequestParam(value ="searchTerm",required = false)String searchTerm
            ,@RequestParam(value="techStacks",required = false) List<String> techStacks
            ,@RequestParam(value ="size",required = false,defaultValue = STUDY_DEFAULT_SIZE) int size
            ,@RequestParam(value="page",required = false,defaultValue = "0") int page
            ,@RequestParam(value = "sort",required = false,defaultValue =STUDY_DEFAULT_SORT) String sort
            ,@RequestParam(value="direction",required = false,defaultValue = "desc")String direction){
        if(direction.equals("desc")){
            return studyService.getStudies(searchTerm,techStacks, PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,sort)));
        }
        return studyService.getStudies(searchTerm,techStacks, PageRequest.of(page,size,Sort.by(Sort.Direction.ASC,sort)));
    }

    @GetMapping (value="/studies/{id}")
    @ApiOperation(value = "스터디 상세보기")
    public GetStudyDto getStudies(@PathVariable("id") Long studyId){
        return studyService.getStudy(studyId);
    }

    @GetMapping (value="/studies/recommend")
    @ApiOperation(value = "스터디 추천 받기")
    public List<GetRecommendedStudyDto> getRecommendedStudy(){
        return studyService.getRecommendedStudy();
    }
}
