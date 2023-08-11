package site.chagok.server.contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.chagok.server.contest.domain.ContestScrap;

import java.util.Optional;

@Repository
public interface ContestScrapRepository extends JpaRepository<ContestScrap, Long> {

    Optional<ContestScrap> findByContestId(Long contest_id);
    Optional<ContestScrap> deleteByContestId(Long contest_id);
}
