package site.chagok.server.project_study.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.chagok.server.project_study.project.domain.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
