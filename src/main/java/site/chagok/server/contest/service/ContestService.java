package site.chagok.server.contest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chagok.server.common.exception.BoardNotFoundException;
import site.chagok.server.contest.dto.*;
import site.chagok.server.contest.exception.CommentNotFoundException;
import site.chagok.server.contest.repository.CommentRepository;
import site.chagok.server.contest.util.CommentSorter;
import site.chagok.server.contest.domain.Comment;
import site.chagok.server.contest.domain.Contest;
import site.chagok.server.contest.repository.ContestRepository;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.exception.InvalidMemberException;
import site.chagok.server.member.service.MemberCredentialService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContestService {

    private final ContestRepository contestRepository;
    private final CommentRepository commentRepository;
    private final MemberCredentialService credentialService;

    @Transactional
    public GetContestDto getContest(Long contestId){
        Contest foundContest = contestRepository.findById(contestId).orElseThrow(BoardNotFoundException::new);
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
                .content(foundContest.getContent())
                .build();
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
        Contest contest = contestRepository.findById(contestId).orElseThrow(BoardNotFoundException::new);

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

    // 공모전 글에 대한 댓글 조회
    @Transactional(readOnly = true)
    public List<GetContestCommentDto> getContestComments(Long contestId){
        Contest contest = contestRepository.findContestByIdFetchCommentsAndMemberName(contestId).orElseThrow(BoardNotFoundException::new);

        List<Comment> comments = contest.getComments();
        return CommentSorter.getSort(comments);
    }


    // 댓글 추가
    @Transactional
    public Long makeComment(CommentDto commentDto){
        Contest contest = contestRepository.findById(commentDto.getContestId()).orElseThrow(BoardNotFoundException::new);
        // 로그인 사용자 조회
        Member member = credentialService.getMember();

        // 댓글 생성 Dto to Entity
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .parentId(commentDto.getParentId())
                .kakaoRef(commentDto.getKakaoRef())
                .contest(contest)
                .member(member)
                .build();

        commentRepository.save(comment);

        // 공모전 댓글에 추가
        contest.getComments().add(comment);
        // 공모전 댓글 개수
        contest.addCommentCount();
        return comment.getId();
    }


    // 댓글 수정
    @Transactional
    public Long updateComment(CommentUpdateDto commentUpdateDto) {

        // 사용자 조회
        Member member = credentialService.getMember();

        // 댓글 조회
        Comment comment = commentRepository.findById(commentUpdateDto.getCommentId()).orElseThrow(CommentNotFoundException::new);

        if (member.getNickName() != comment.getMember().getNickName())
            throw new InvalidMemberException();

        comment.updateComment(commentUpdateDto.getContent(), commentUpdateDto.getKakaoRef());

        return comment.getId();
    }

    // 댓글 삭제
    @Transactional
    public Long deleteComment(Long commentId) {

        // 사용자 조회
        Member member = credentialService.getMember();

        // 댓글 조회
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.getContest().minusCommentCount();

        if (member.getNickName() != comment.getMember().getNickName())
            throw new InvalidMemberException();

        comment.setDeleted();

        return comment.getId();
    }
}
