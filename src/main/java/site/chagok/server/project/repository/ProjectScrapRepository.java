package site.chagok.server.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.chagok.server.project.domain.ProjectScrap;

import java.util.Optional;

@Repository
public interface ProjectScrapRepository extends JpaRepository<ProjectScrap, Long> {

    Optional<ProjectScrap> deleteByProjectId(Long projectId);
}
