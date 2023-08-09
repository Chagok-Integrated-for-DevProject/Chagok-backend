package site.chagok.server.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.chagok.server.study.domain.Study;

import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {

    Optional<Study> findByTitle(String title);
}
