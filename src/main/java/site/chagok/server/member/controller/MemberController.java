package site.chagok.server.member.controller;

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
    @ApiOperation(value="닉네임 업데이트")
    @ApiImplicitParam(name = "nickname", value = "변경할 닉네임")
    @ApiResponses({@ApiResponse(code = 200, message = "닉네임 변경성공"), @ApiResponse(code = 409, message = "error code, 이미 존재하는 닉네임")})
    public ResponseEntity changeNickName(@RequestParam("nickname")String nickName) {

        try {
            memberService.updateNickName(nickName);
        } catch (NickNameExistsException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/scrap")
    @ApiOperation(value = "게시글 스크랩 추가")
    @ApiResponses({@ApiResponse(code = 200, message = "스크랩 추가 성공"), @ApiResponse(code = 400, message = "error code 스크랩 추가 오류")})
    public ResponseEntity scrapBoard(@RequestBody BoardScrap boardScrap) {

        try {
            memberService.addBoardScrap(boardScrap);
        } catch (IllegalStateException e) {
            return new ResponseEntity("invalid category type", HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity("invalid board id", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
