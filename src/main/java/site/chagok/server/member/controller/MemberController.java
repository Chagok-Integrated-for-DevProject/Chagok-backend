package site.chagok.server.member.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chagok.server.member.constants.ActionType;
import site.chagok.server.member.dto.BoardScrapDto;
import site.chagok.server.member.dto.MemberInfoDto;
import site.chagok.server.member.exception.NickNameExistsException;
import site.chagok.server.member.service.ImgService;
import site.chagok.server.member.service.MemberInfoService;
import site.chagok.server.member.service.MemberService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;


@Api(tags ="사용자 데이터 조회")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final ImgService imgService;
    private final MemberInfoService memberInfoService;

    @GetMapping("/info")
    @ApiOperation(value = "secure - 사용자 정보조회(마이페이지)")
    public MemberInfoDto getMemberInfo() {

        MemberInfoDto memberInfoDto = memberInfoService.getMemberInfoDto();

        return memberInfoDto;
    }

    @GetMapping("/profile/{image}")
    @ApiOperation(value = "사용자 이미지 조회")
    @ApiResponses({@ApiResponse(code = 200, message = "스크랩 조회 성공"), @ApiResponse(code = 400, message = "프로필 조회 오류")})
    public ResponseEntity getProfile(@PathVariable("image")String image) {

        byte[] savedFile = null;
        MediaType mediaType = null;
        try {
            savedFile = imgService.getProfileImg(image);
            mediaType = imgService.getMediaType(image);
        } catch (IOException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().contentType(mediaType).body(savedFile);
    }
}
