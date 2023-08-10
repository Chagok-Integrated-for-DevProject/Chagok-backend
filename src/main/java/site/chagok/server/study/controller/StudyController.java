package site.chagok.server.study.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.chagok.server.study.dto.GetStudyPreviewDto;
import site.chagok.server.study.service.StudyService;

import java.util.List;



@Api(tags = "스터디")
@RequiredArgsConstructor
@RestController
public class StudyController {
    private final StudyService studyService;
    private static final int STUDY_DEFAULT_SIZE =3;
    private static final String STUDY_DEFAULT_SORT ="hotCount";
    @GetMapping(value="/studies")
    @ApiOperation(value = "스터디 정렬",notes = "파라미터 searchTerm(검색어),pageNumber(기본값 0),pageSize(기본값 3),sort(기본값 hotCount,desc / 마감순은 id,desc)")
    public Page<GetStudyPreviewDto> getStudy(@RequestParam(value ="searchTerm",required = false)String searchTerm
    ,@RequestParam(value="techStacks",required = false) List<String> techStacks
    ,@PageableDefault(size =STUDY_DEFAULT_SIZE,sort = STUDY_DEFAULT_SORT,direction = Sort.Direction.DESC) Pageable pageable){
        return studyService.getStudy(searchTerm,techStacks,pageable);
    }
}
