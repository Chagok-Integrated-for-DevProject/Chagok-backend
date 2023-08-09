package site.chagok.server.contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.chagok.server.contest.domain.Contest;

import java.util.Optional;


@Repository
public interface ContestRepository extends JpaRepository<Contest,Long> {

    @Query("select c from Contest c left join fetch c.comments cm left join fetch cm.member where c.id = :contestId")
    Optional<Contest> findContestByIdFetchCommentsAndMemberName(@Param("contestId") Long contestId);
}
