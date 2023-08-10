package site.chagok.server.contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.chagok.server.contest.domain.ContestScrap;

@Repository
public interface ContestScrapRepository extends JpaRepository<ContestScrap, Long> {
}
