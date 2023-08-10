package site.chagok.server.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.chagok.server.study.domain.StudyScrap;

@Repository
public interface StudyScrapRepository extends JpaRepository<StudyScrap, Long> {
}
