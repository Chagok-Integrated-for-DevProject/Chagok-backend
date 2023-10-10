package site.chagok.server.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.chagok.server.member.domain.Member;
import site.chagok.server.project.domain.Project;
import site.chagok.server.project.domain.ProjectScrap;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectScrapRepository extends JpaRepository<ProjectScrap, Long> {

    Optional<ProjectScrap> deleteByProjectId(Long projectId);


    @Query(value = "select distinct p from ProjectScrap ps inner join Project p on ps.project = p join fetch p.techStacks where ps.member = :member")
    List<Project> findProjectByMemberWithTechs(@Param("member") Member member);
}
