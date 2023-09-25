package site.chagok.server.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.chagok.server.project.domain.Project;
import site.chagok.server.study.domain.Study;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {


    Page<Project> findAll(Specification<Project> spec, Pageable pageable);
    @Query("select p from Project p where p.techStacks in (:techStack)")
    List<Project> getRecommendedProject(@Param("techStack") List<String> techStack);

}
