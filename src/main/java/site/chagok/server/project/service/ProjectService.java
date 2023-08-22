package site.chagok.server.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.common.contstans.PostType;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.repository.MemberRepository;
import site.chagok.server.member.util.MemberCredential;
import site.chagok.server.project.domain.Project;
import site.chagok.server.project.dto.GetProjectDto;
import site.chagok.server.project.dto.GetProjectPreviewDto;
import site.chagok.server.project.dto.GetRecommendedProjectDto;
import site.chagok.server.project.repository.ProjectRepository;
import site.chagok.server.project.repository.ProjectSpecification;
import site.chagok.server.study.domain.Study;
import site.chagok.server.study.dto.GetRecommendedStudyDto;
import site.chagok.server.study.dto.GetStudyPreviewDto;
import site.chagok.server.study.repository.StudySpecification;


import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public Page<GetProjectPreviewDto> getProjects(String searchTerm, List<String> techStacks, Pageable pageable){
        Specification<Project> spec  = (root, query, criteriaBuilder) ->null;
        if(searchTerm!=null) {
            spec = spec.and(ProjectSpecification.equalsTitle(searchTerm));
        }
        if(techStacks!=null && !techStacks.isEmpty()){
            spec = spec.and(ProjectSpecification.equalsTechStack(techStacks));
        }
        Page<Project> projects = projectRepository.findAll(spec,pageable);
        return projects.map(s-> GetProjectPreviewDto.builder()
                .projectId(s.getId())
                .title(s.getTitle())
                .preview(s.getContent()) // 추후 수정
                .siteType(s.getSiteType())
                .techStacks(s.getTechStacks())
                .viewCount(s.getViewCount())
                .scrapCount(s.getScrapCount())
                .nickName(s.getNickname())
                .postType(PostType.PROJECT)
                .createdTime(s.getCreatedTime())
                .build());
    }

    // 사용자 프로젝트 스크랩 미리보기
    @Transactional(readOnly = true)
    public GetProjectPreviewDto getProjectPreview(Long projectId) {

        Project project = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);

        return GetProjectPreviewDto.builder()
                .title(project.getTitle())
                .preview(project.getContent()) // 추후 수정
                .siteType(project.getSiteType())
                .postType(PostType.PROJECT)
                .techStacks(project.getTechStacks())
                .viewCount(project.getViewCount())
                .scrapCount(project.getScrapCount())
                .projectId(project.getId())
                .nickName(project.getNickname())
                .build();
    }

    @Transactional
    public List<GetRecommendedProjectDto> getRecommendedProject(){
        String userEmail = MemberCredential.getLoggedMemberEmail();
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(EntityNotFoundException::new);
        return projectRepository.getRecommendedProject(member.getTechStacks()).stream().map(
                p-> GetRecommendedProjectDto.builder()
                        .projectId(p.getId())
                        .title(p.getTitle())
                        .build()
        ).collect(Collectors.toList());
    }

    @Transactional
    public GetProjectDto getProject(Long studyId){
        Project project = projectRepository.findById(studyId).orElseThrow(EntityNotFoundException::new);
        project.addViewCount();
        return GetProjectDto.builder()
                .title(project.getTitle())
                .nickName(project.getNickname())
                .siteType(project.getSiteType())
                .createdTime(project.getCreatedTime())
                .content(project.getContent())
                .scrapCount(project.getScrapCount())
                .sourceUrl(project.getSourceUrl())
                .viewCount(project.getViewCount())
                .techStacks(project.getTechStacks())
                .build();
    }


}
