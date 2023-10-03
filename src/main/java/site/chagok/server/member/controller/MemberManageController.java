package site.chagok.server.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.chagok.server.common.dto.ErrorDto;
import site.chagok.server.member.constants.ActionType;
import site.chagok.server.member.dto.BoardScrapDto;
import site.chagok.server.member.service.MemberImgService;
import site.chagok.server.member.service.MemberUpdateService;

import java.io.IOException;
import java.util.List;


@Tag(name = "사용자 데이터 관리 API")
@RestController
@RequestMapping("/member/update")
@RequiredArgsConstructor
public class MemberManageController {

    /*
    (사용자 식별)
    -마이페이지 기능 엔드포인트
       이미지 수정, 닉네임 수정, 기술태크 수정, 스크랩
     */

    private final MemberUpdateService memberUpdateService;
    private final MemberImgService memberImgService;


    @PostMapping("/nickname")
    @Operation(summary="secure - 닉네임 업데이트")
    @Parameter(name = "nickname", description = "변경할 닉네임")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "닉네임 변경성공"),
            @ApiResponse(responseCode = "400", description = "nickname_01 - 해당 닉네임 중복")
    })
    public void updateNickName(@RequestParam("nickname")String nickName) {

        memberUpdateService.updateNickName(nickName);
    }

    @PostMapping("/scrap")
    @Operation(summary = "secure - 게시글 스크랩 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스크랩 추가 성공"),
            @ApiResponse(responseCode = "404", description = "board_01 - 게시글 조회 오류", content = @Content(
                    schema = @Schema(implementation = ErrorDto.class))),
    })
    public void addScrapBoard(@RequestBody BoardScrapDto boardScrapDto) {

        memberUpdateService.manageBoardScrap(boardScrapDto, ActionType.POST);
    }

    @DeleteMapping("/scrap")
    @Operation(summary = "secure - 게시글 스크랩 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스크랩 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "scrap_01 - 스크랩 조회 오류", content = @Content(
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    public void deleteScrapBoard(@RequestBody BoardScrapDto boardScrapDto) {

        memberUpdateService.manageBoardScrap(boardScrapDto, ActionType.DELETE);
    }

    @PostMapping("/skills")
    @Operation(summary = "secure - 사용자 기술스택 업데이트", description = "request body: skills - 사용자 기술 스택에 대한 String list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스크랩 추가 성공")
    })
    public void updateTechStacks(@RequestBody List<String> skills) {

        memberUpdateService.updateTechStacks(skills);
    }

    @PostMapping("/profile-image")
    @Operation(summary = "secure - 사용자 프로필 이미지 업데이트", description = "request body: image - 사용자가 업데이트할 프로필 이미지( multipart 형식 )")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 이미지 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "image_01 - 프로필 이미지 업데이트 오류", content = @Content(
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    public void updateProfileImg(@RequestPart(name = "image")MultipartFile profileFile) throws IOException {

        memberImgService.updateProfileImg(profileFile);
    }

    @DeleteMapping("/profile-image")
    @Operation(summary = "secure - 사용자 프로필 이미지 삭제, 사용자의 프로필 이미지 null처리")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스크랩 삭제 성공"),
    })
    public void deleteProfileImg() {

        memberImgService.deleteProfileImg();
    }
}
