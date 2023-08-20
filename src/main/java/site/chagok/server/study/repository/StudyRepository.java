package site.chagok.server.study.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.chagok.server.study.domain.Study;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study,Long>, JpaSpecificationExecutor<Study> {
    Optional<Study> findByTitle(String title);
    Page<Study> findAll(Specification<Study> spec, Pageable pageable);



    @Query("select s from Study s where s.techStacks in :techStack")
    List<Study> getRecommendedStudy(@Param("techStack") List<String> techStack);
}
