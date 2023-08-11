package site.chagok.server.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.chagok.server.study.domain.StudyScrap;

import java.util.Optional;

@Repository
public interface StudyScrapRepository extends JpaRepository<StudyScrap, Long> {

    Optional<StudyScrap> deleteByStudyId(Long study_id);
}
