package site.chagok.server.contest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.chagok.server.contest.domain.Contest;
import site.chagok.server.contest.dto.GetContestDto;
import site.chagok.server.contest.repository.ContestRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ContestService {

    private final ContestRepository contestRepository;
    public GetContestDto getContest(Long contestId){
        Optional<Contest> contest = contestRepository.findById(contestId);
        if(!contest.isPresent()) throw new EntityNotFoundException();
        Contest foundContest = contest.get();
        return GetContestDto.builder()
                .title(foundContest.getTitle())
                .imageUrl(foundContest.getImageUrl())
                .originalUrl(foundContest.getOriginalUrl())
                .host(foundContest.getHost())
                .startDate(foundContest.getStartDate())
                .endDate(foundContest.getEndDate())
                .viewCount(foundContest.getViewCount())
                .scrapCount(foundContest.getScrapCount())
                .build();
    }
}
