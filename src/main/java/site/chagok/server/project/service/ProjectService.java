package site.chagok.server.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.project.domain.Project;
import site.chagok.server.project.dto.GetProjectPreviewDto;
import site.chagok.server.project.repository.ProjectRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;



    // 사용자 프로젝트 스크랩 미리보기
    @Transactional(readOnly = true)
    public GetProjectPreviewDto getProjectPreview(Long projectId) {

        Project project = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);

        return GetProjectPreviewDto.builder()
                .title(project.getTitle())
                .preview(project.getContent()) // 추후 수정
                .siteType(project.getSiteType())
                .techStacks(project.getTechStacks())
                .viewCount(project.getViewCount())
                .scrapCount(project.getScrapCount())
                .studyId(project.getId())
                .nickName(project.getNickname())
                .build();
    }


}
