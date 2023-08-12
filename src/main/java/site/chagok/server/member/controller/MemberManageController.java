package site.chagok.server.member.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.chagok.server.member.constants.ActionType;
import site.chagok.server.member.dto.BoardScrapDto;
import site.chagok.server.member.exception.NickNameExistsException;
import site.chagok.server.member.service.MemberService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;


@Api(tags ="사용자 데이터 관리")
@RestController
@RequestMapping("/member/update")
@RequiredArgsConstructor
public class MemberManageController {

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
    public ResponseEntity updateNickName(@RequestParam("nickname")String nickName) {

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
    public ResponseEntity addScrapBoard(@RequestBody BoardScrapDto boardScrapDto) {

        try {
            memberService.manageBoardScrap(boardScrapDto, ActionType.POST);
        } catch (IllegalStateException e) {
            return new ResponseEntity("invalid category type", HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity("invalid board id", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/scrap")
    @ApiOperation(value = "게시글 스크랩 삭제")
    @ApiResponses({@ApiResponse(code = 200, message = "스크랩 삭제 성공"), @ApiResponse(code = 400, message = "error code 스크랩 삭제 오류")})
    public ResponseEntity deleteScrapBoard(@RequestBody BoardScrapDto boardScrapDto) {

        try {
            memberService.manageBoardScrap(boardScrapDto, ActionType.DELETE);
        } catch (IllegalStateException e) {
            return new ResponseEntity("invalid category type", HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity("invalid board id", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }


    // 사용자 스택 추가
    @PostMapping("/skills")
    @ApiOperation(value = "사용자 기술스택 업데이트")
    @ApiResponses({@ApiResponse(code = 200, message = "스크랩 추가 성공")})
    public ResponseEntity updateTechStacks(@RequestBody List<String> skills) {

        memberService.updateTechStacks(skills);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 이미지 저장
    @PostMapping("/profile/image")
    @ApiOperation(value = "사용자 프로필 이미지 업데이트")
    @ApiResponses({@ApiResponse(code = 200, message = "스크랩 삭제 성공"), @ApiResponse(code = 400, message = "프로필 이미지 업데이트 오류")})
    public ResponseEntity updateProfile(@RequestPart(name = "image")MultipartFile profileFile) {

        try {
            memberService.updateProfileImg(profileFile);
        } catch (IOException e) {
            return new ResponseEntity("cannot update profile image file", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}