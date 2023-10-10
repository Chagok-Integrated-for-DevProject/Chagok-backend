package site.chagok.server.contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.chagok.server.contest.domain.Contest;
import site.chagok.server.contest.domain.ContestScrap;
import site.chagok.server.member.domain.Member;
import site.chagok.server.project.domain.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestScrapRepository extends JpaRepository<ContestScrap, Long> {

    Optional<ContestScrap> findByContestId(Long contest_id);
    Optional<ContestScrap> deleteByContestId(Long contest_id);

    @Query(value = "select c from ContestScrap cs inner join Contest c on cs.contest = c where cs.member = :member")
    List<Contest> findContestByMember(@Param("member") Member member);
}
