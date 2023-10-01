package site.chagok.server.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.common.contstans.PostType;
import site.chagok.server.common.exception.BoardNotFoundApiException;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.service.MemberCredentialService;
import site.chagok.server.study.domain.Study;
import site.chagok.server.study.dto.GetRecommendedStudyDto;
import site.chagok.server.study.dto.GetStudyDto;
import site.chagok.server.study.dto.GetStudyPreviewDto;
import site.chagok.server.study.repository.StudyRepository;
import site.chagok.server.study.repository.StudySpecification;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberCredentialService credentialService;

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
                .studyId(s.getId())
                .title(s.getTitle())
                .preview(s.getContent()) // 추후 수정
                .siteType(s.getSiteType())
                .techStacks(s.getTechStacks())
                .viewCount(s.getViewCount())
                .scrapCount(s.getScrapCount())
                .postType(PostType.STUDY)
                .createdTime(s.getCreatedTime())
                .build());
    }
    @Transactional
    public List<GetRecommendedStudyDto> getRecommendedStudy(){

        Member member = credentialService.getMember();
        return studyRepository.getRecommendedStudy(member.getTechStacks()).stream().map(
                s-> GetRecommendedStudyDto.builder()
                        .studyId(s.getId())
                        .title(s.getTitle())
                        .build()
        ).collect(Collectors.toList());
    }
    // 사용자 스터디 스크랩 미리보기
    @Transactional(readOnly = true)
    public GetStudyPreviewDto getStudyPreview(Long studyId) {

        Study study = studyRepository.findById(studyId).orElseThrow(BoardNotFoundApiException::new);

        return GetStudyPreviewDto.builder()
                .studyId(study.getId())
                .title(study.getTitle())
                .preview(study.getContent()) // 추후 수정
                .siteType(study.getSiteType())
                .postType(PostType.STUDY)
                .techStacks(study.getTechStacks())
                .viewCount(study.getViewCount())
                .scrapCount(study.getScrapCount())
                .studyId(study.getId())
                .build();
    }
    @Transactional
    public GetStudyDto getStudy(Long studyId){
        Study study = studyRepository.findById(studyId).orElseThrow(BoardNotFoundApiException::new);
        study.addViewCount();
        return GetStudyDto.builder()
                .title(study.getTitle())
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
