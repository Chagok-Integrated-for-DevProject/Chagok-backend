package site.chagok.server.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.chagok.server.project.domain.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
