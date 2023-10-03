package site.chagok.server.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chagok.server.common.dto.ErrorDto;
import site.chagok.server.member.dto.MemberInfoDto;
import site.chagok.server.member.service.MemberImgService;
import site.chagok.server.member.service.MemberInfoService;
import site.chagok.server.member.util.MediaTypeSelector;


@Tag(name = "사용자 데이터 조회 API")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberImgService memberImgService;
    private final MemberInfoService memberInfoService;

    @GetMapping("/info")
    @Operation(summary = "secure - 사용자 정보조회(마이페이지)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스크랩 조회 성공")
    })
    public MemberInfoDto getMemberInfo() {

        return memberInfoService.getMemberInfoDto();
    }

    @GetMapping("/profile/{image}")
    @Operation(summary = "사용자 이미지 조회")
    @Parameter(name = "image", description = "조회할 사용자 프로필 이미지( 파일이름.확장자 로 이루어짐 )")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 이미지 조회 성공"),
            @ApiResponse(responseCode = "404", description = "image_01 - 프로필 조회 오류", content = @Content(
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    public ResponseEntity getProfileImg(@PathVariable("image")String image){

        byte[] savedFile = memberImgService.getProfileImg(image);
        MediaType mediaType = MediaTypeSelector.getMediaType(image);

        return ResponseEntity.ok().contentType(mediaType).body(savedFile);
    }

    @GetMapping("/check/nickname")
    @Operation(summary = "사용자 닉네임 중복 여부 확인")
    @Parameter(name = "nickname", description = "사용자 중복 확인할 닉네임")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "해당 닉네임 사용 가능"),
            @ApiResponse(responseCode = "400", description = "nickname_01 - 해당 닉네임 중복", content = @Content(
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    public ResponseEntity checkNickName(@RequestParam("nickname") String nickName) {

        // 해당 닉네임이 존재하는지 체크
        memberInfoService.checkNicknameExists(nickName);

        return new ResponseEntity(HttpStatus.OK);
    }

}
