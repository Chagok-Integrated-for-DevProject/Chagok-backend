package site.chagok.server.member.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chagok.server.member.dto.MemberInfoDto;
import site.chagok.server.member.service.MemberImgService;
import site.chagok.server.member.service.MemberInfoService;
import site.chagok.server.member.util.MediaTypeSelector;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;


@Api(tags ="사용자 데이터 조회")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberImgService memberImgService;
    private final MemberInfoService memberInfoService;

    @GetMapping("/info")
    @ApiOperation(value = "secure - 사용자 정보조회(마이페이지)", response = MemberInfoDto.class)
    @ApiResponses({@ApiResponse(code = 200, message = "스크랩 조회 성공"), @ApiResponse(code = 400, message = "사용자 조회 오류")})
    public ResponseEntity<MemberInfoDto> getMemberInfo() {

        try {
            MemberInfoDto memberInfoDto = memberInfoService.getMemberInfoDto();

            return ResponseEntity.ok(memberInfoDto);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity("invalid member", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/profile/{image}")
    @ApiOperation(value = "사용자 이미지 조회")
    @ApiImplicitParam(name = "image", value = "조회할 사용자 프로필 이미지( 파일이름.확장자 로 이루어짐 )")
    @ApiResponses({@ApiResponse(code = 200, message = "스크랩 조회 성공"), @ApiResponse(code = 400, message = "프로필 조회 오류")})
    public ResponseEntity getProfileImg(@PathVariable("image")String image) {

        byte[] savedFile = null;
        MediaType mediaType = null;
        try {
            savedFile = memberImgService.getProfileImg(image);
            mediaType = MediaTypeSelector.getMediaType(image);
        } catch (IOException e) {
            return new ResponseEntity("cannot get profile image", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().contentType(mediaType).body(savedFile);
    }

    @GetMapping("/check/nickname")
    @ApiOperation(value = "사용자 닉네임 중복 여부 확인")
    @ApiImplicitParam(name = "nickname", value = "사용자 중복 확인할 닉네임")
    @ApiResponses({@ApiResponse(code = 200, message = "닉네임 변경 가능"), @ApiResponse(code = 400, message = "닉네임 중복 오류")})
    public ResponseEntity checkNickName(@RequestParam("nickname") String nickName) {

        if (memberInfoService.checkNicknameExists(nickName)) // 해당 닉네임이 이미 존재한다면..
            return new ResponseEntity("cannot update nickname", HttpStatus.BAD_REQUEST);

        return new ResponseEntity(HttpStatus.OK);
    }

}
