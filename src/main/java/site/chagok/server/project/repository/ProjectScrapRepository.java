package site.chagok.server.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.chagok.server.project.domain.ProjectScrap;

@Repository
public interface ProjectScrapRepository extends JpaRepository<ProjectScrap, Long> {
}
