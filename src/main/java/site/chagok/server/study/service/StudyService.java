package site.chagok.server.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.study.domain.Study;
import site.chagok.server.study.dto.GetStudyDto;
import site.chagok.server.study.dto.GetStudyPreviewDto;
import site.chagok.server.study.repository.StudyRepository;
import site.chagok.server.study.repository.StudySpecification;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    @Transactional
    public Page<GetStudyPreviewDto> getStudies(String searchTerm, List<String> techStacks, Pageable pageable){
        Specification<Study> spec  = (root, query, criteriaBuilder) ->null;
        if(searchTerm!=null) {
            spec = spec.and(StudySpecification.equalsTitle(searchTerm));
        }
        if(techStacks!=null && !techStacks.isEmpty()){
            spec = spec.and(StudySpecification.equalsTechStack(techStacks));
        }
        Page<Study> studies = studyRepository.findAll(spec,pageable);
        return studies.map(s-> GetStudyPreviewDto.builder()
                .title(s.getTitle())
                .preview(s.getContent()) // 추후 수정
                .siteType(s.getSiteType())
                .techStacks(s.getTechStacks())
                .viewCount(s.getViewCount())
                .scrapCount(s.getScrapCount())
                .studyId(s.getId())
                .nickName(s.getNickname())
                .build());
    }

    // 사용자 스터디 스크랩 미리보기
    @Transactional(readOnly = true)
    public GetStudyPreviewDto getStudyPreview(Long studyId) {

        Study study = studyRepository.findById(studyId).orElseThrow(EntityNotFoundException::new);

        return GetStudyPreviewDto.builder()
                .title(study.getTitle())
                .preview(study.getContent()) // 추후 수정
                .siteType(study.getSiteType())
                .techStacks(study.getTechStacks())
                .viewCount(study.getViewCount())
                .scrapCount(study.getScrapCount())
                .studyId(study.getId())
                .nickName(study.getNickname())
                .build();
    }


    @Transactional
    public GetStudyDto getStudy(Long studyId){
        Study study = studyRepository.findById(studyId).orElseThrow(EntityNotFoundException::new);
        study.addViewCount();
        return GetStudyDto.builder()
                .title(study.getTitle())
                .nickName(study.getNickname())
                .siteType(study.getSiteType())
                .createdTime(study.getCreatedTime())
                .content(study.getContent())
                .scrapCount(study.getScrapCount())
                .sourceUrl(study.getSourceUrl())
                .viewCount(study.getViewCount())
                .techStacks(study.getTechStacks())
                .build();
    }
}
