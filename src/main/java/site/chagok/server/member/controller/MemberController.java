package site.chagok.server.member.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.chagok.server.member.exception.NickNameExistsException;
import site.chagok.server.member.repository.MemberRepository;
import site.chagok.server.member.service.MemberService;


@Api(tags ="사용자 데이터 관리")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    /*
    (사용자 식별)
    -마이페이지 기능 엔드포인트
       이미지 수정, 닉네임 수정, 기술태크 수정, 스크랩
     */

    private final MemberService memberService;

    @GetMapping("/nickname")
    @ApiOperation(value="닉네임 업데이트", notes = "파라미터 nickname = 변경할 닉네임, 이미 존재하는 닉네임이라면, 409 error")
    public ResponseEntity changeNickName(@RequestParam("nickname")String nickName) {

        try {
            memberService.updateNickName(nickName);
        } catch (NickNameExistsException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/scrap")
    @ApiOperation(value = "게시글 스크랩", notes = "파라미터  catergory = ( study, project, contest ) , 파라미터 id = 스크랩 삭제할 게시글 id")
    public ResponseEntity scrapBoard(@RequestParam("category") String category, Long id) {
        memberService.addBoardScrap(category, id);

        return new ResponseEntity(HttpStatus.OK);
    }
}
