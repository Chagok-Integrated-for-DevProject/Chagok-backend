package site.chagok.server.contest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.contest.CommentSorter;
import site.chagok.server.contest.domain.Comment;
import site.chagok.server.contest.domain.Contest;
import site.chagok.server.contest.dto.CommentDto;
import site.chagok.server.contest.dto.GetContestCommentDto;
import site.chagok.server.contest.dto.GetContestDto;
import site.chagok.server.contest.dto.GetContestPreviewDto;
import site.chagok.server.contest.repository.ContestRepository;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

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
                .originalUrl(foundContest.getSourceUrl())
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
        Contest contest = contestRepository.findById(commentDto.getContestId()).orElseThrow(EntityNotFoundException::new);
        Member member = new Member();
        memberRepository.save(member);
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .parentId(commentDto.getParentId())
                .contest(contest)
                .member(member)
                .build();
        contest.getComments().add(comment);
        contest.addCommentCount();
        return comment.getId();
    }
    @Transactional
    public void makeContest(){
        Contest contest = new Contest();
        contestRepository.save(contest);
    }
    @Transactional(readOnly = true)
    public Page<GetContestPreviewDto> getContests(String searchTerm,Pageable pageable){
        Page<Contest> contests = contestRepository.findByTitleContaining(searchTerm,pageable);
        return contests.map(c-> GetContestPreviewDto.builder()
                .contestId(c.getId())
                .title(c.getTitle())
                .imageUrl(c.getImageUrl())
                .host(c.getHost())
                .startDate(c.getStartDate().toString())
                .endDate(c.getEndDate().toString())
                .scrapCount(c.getScrapCount())
                .commentCount(c.getCommentCount())
                .build());
    }
}
