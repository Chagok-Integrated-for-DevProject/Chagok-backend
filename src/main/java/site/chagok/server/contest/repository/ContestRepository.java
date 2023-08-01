package site.chagok.server.contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.chagok.server.contest.domain.Contest;


@Repository
public interface ContestRepository extends JpaRepository<Contest,Long> {
}
