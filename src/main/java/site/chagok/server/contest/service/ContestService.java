package site.chagok.server.contest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.contest.util.CommentSorter;
import site.chagok.server.contest.domain.Comment;
import site.chagok.server.contest.domain.Contest;
import site.chagok.server.contest.dto.CommentDto;
import site.chagok.server.contest.dto.GetContestCommentDto;
import site.chagok.server.contest.dto.GetContestDto;
import site.chagok.server.contest.dto.GetContestPreviewDto;
import site.chagok.server.contest.repository.ContestRepository;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.repository.MemberRepository;
import site.chagok.server.member.util.MemberCredential;

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
        foundContest.addViewCount();
        return GetContestDto.builder()
                .contestId(foundContest.getId())
                .title(foundContest.getTitle())
                .imageUrl(foundContest.getImageUrl())
                .originalUrl(foundContest.getSourceUrl())
                .host(foundContest.getHost())
                .startDate(foundContest.getStartDate())
                .endDate(foundContest.getEndDate())
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

        // 로그인 사용자 조회
        String email = MemberCredential.getLoggedMemberEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

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
    public Page<GetContestPreviewDto> getContests(Pageable pageable){
        Page<Contest> contests = contestRepository.findAll(pageable);
        return contests.map(c-> GetContestPreviewDto.builder()
                .contestId(c.getId())
                .title(c.getTitle())
                .imageUrl(c.getImageUrl())
                .host(c.getHost())
                .startDate(c.getStartDate())
                .endDate(c.getEndDate())
                .scrapCount(c.getScrapCount())
                .commentCount(c.getCommentCount())
                .build());
    }

    // 사용자 공모전 스크랩 미리보기
    @Transactional(readOnly = true)
    public GetContestPreviewDto getContestPreview(Long contestId) {
        Contest contest = contestRepository.findById(contestId).orElseThrow(EntityNotFoundException::new);

        // 공모전 스크랩 미리보기 dto 반환
        return GetContestPreviewDto.builder()
                .contestId(contest.getId())
                .title(contest.getTitle())
                .imageUrl(contest.getImageUrl())
                .host(contest.getHost())
                .startDate(contest.getStartDate())
                .endDate(contest.getEndDate())
                .scrapCount(contest.getScrapCount())
                .commentCount(contest.getCommentCount())
                .build();
    }
}
