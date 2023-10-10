package site.chagok.server.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.chagok.server.member.domain.Member;
import site.chagok.server.project.domain.Project;
import site.chagok.server.study.domain.Study;
import site.chagok.server.study.domain.StudyScrap;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyScrapRepository extends JpaRepository<StudyScrap, Long> {

    Optional<StudyScrap> deleteByStudyId(Long studyId);

    @Query(value = "select distinct s from StudyScrap ss inner join Study s on ss.study = s join fetch s.techStacks where ss.member = :member")
    List<Study> findStudyByMemberWithTechs(@Param("member") Member member);
}
