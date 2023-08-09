package site.chagok.server.contest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.contest.CommentSorter;
import site.chagok.server.contest.domain.Comment;
import site.chagok.server.contest.domain.Contest;
import site.chagok.server.contest.dto.CommentDto;
import site.chagok.server.contest.dto.GetContestCommentDto;
import site.chagok.server.contest.dto.GetContestDto;
import site.chagok.server.contest.repository.ContestRepository;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ContestService {

    private final ContestRepository contestRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public GetContestDto getContest(Long contestId){
        Contest foundContest = contestRepository.findById(contestId).orElseThrow(EntityNotFoundException::new);
        foundContest.addViewCount(1);
        return GetContestDto.builder()
                .title(foundContest.getTitle())
                .imageUrl(foundContest.getImageUrl())
                .originalUrl(foundContest.getOriginalUrl())
                .host(foundContest.getHost())
                .startDate(foundContest.getStartDate().toString())
                .endDate(foundContest.getEndDate().toString())
                .viewCount(foundContest.getViewCount())
                .scrapCount(foundContest.getScrapCount())
                .build();
    }
    @Transactional(readOnly = true)
    public List<GetContestCommentDto> getContestComments(Long contestId){
        Optional<Contest> contest = contestRepository.findContestByIdFetchCommentsAndMemberName(contestId);
        if(!contest.isPresent()) throw new EntityNotFoundException();
        List<Comment> comments = contest.get().getComments();
        return CommentSorter.getSort(comments);
    }

    @Transactional
    public Long makeComment(CommentDto commentDto){
        Optional<Contest> contest = contestRepository.findById(commentDto.getContestId());
        if(!contest.isPresent()) throw new EntityNotFoundException();
        Member member = new Member();
        memberRepository.save(member);
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .parentId(commentDto.getParentId())
                .contest(contest.get())
                .member(member)
                .build();
        contest.get().getComments().add(comment);
        return comment.getId();
    }
    @Transactional
    public void makeContest(){
        Contest contest = new Contest();
        contestRepository.save(contest);
    }
}
