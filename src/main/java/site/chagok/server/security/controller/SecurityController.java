package site.chagok.server.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chagok.server.common.dto.ErrorDto;
import site.chagok.server.security.constants.SecurityHeader;
import site.chagok.server.security.domain.AuthInfo;
import site.chagok.server.security.dto.JwtTokenSetDto;
import site.chagok.server.security.dto.ReqSignInDto;
import site.chagok.server.security.dto.ResSignInDto;
import site.chagok.server.security.dto.SignUpDto;
import site.chagok.server.security.service.AccountService;
import site.chagok.server.security.service.AuthService;
import site.chagok.server.security.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "인증 및 회원가입/탈퇴 관리 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final AuthService authService;
    private final AccountService accountService;

    @PostMapping("/signIn")
    @Operation(summary = "로그인", description = "가입이 되지 않은 상태라면, isSignUp 이 false, 가입이 되어있는 상태라면, isSignUp이 true 및 jwt, refresh 토큰(쿠키:refreshToken - httpOnly, secure 적용) 발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "서버 인증 성공"),
            @ApiResponse(responseCode = "401", description = "auth_01 - 사용자 정보 획득 에러(oauth2.0 통신과정) 또는 access code 에러", content = @Content(
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    public ResponseEntity<ResSignInDto> signIn(@RequestBody ReqSignInDto reqSignInDto) {

        AuthInfo authInfo = authService.signIn(reqSignInDto);

        if (authInfo.isSignUp()) // 가입이 된 상태라서, 바로 로그인 완료 및 jwt 발급
            return ResponseUtil.createResponseWithCookieAndBody(authInfo);

        return ResponseEntity.ok().body(authInfo.getSignInResDto()); // 가입이 되지 않은 상태
    }

    @PostMapping("/refresh")
    @Operation(summary = "secure - 리프레시 토큰으로 jwt 토큰 갱신")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리프레시 토큰 갱신 성공"),
            @ApiResponse(responseCode = "401", description = "refresh_01 - 서버측에 refresh token 이 없거나 유효하지 않은 refresh 토큰 요청 할 때" +
                    " \t\n refresh_02 - 토큰 갱신 요청시 refresh token이 아직 만료되지 않았을 때" +
                    " \t\n jwt_01 - jwt access token 에러", content = @Content(
                    schema = @Schema(implementation = ErrorDto.class))),
    })
    public ResponseEntity<ResSignInDto> renewRefreshTokenAuth(@CookieValue("refreshToken") String refreshToken, HttpServletRequest request) {

        String jwtToken = request.getHeader(SecurityHeader.JWT_HEADER).substring(7);

        return ResponseUtil.createResponseWithCookieAndBody(authService.refresh(new JwtTokenSetDto(jwtToken, refreshToken)));
    }

    @PostMapping("/signUp")
    @Operation(summary = "사용자 회원가입", description = "회원가입 성공 시, jwt, refresh 토큰(쿠키:refreshToken - httpOnly, secure 적용) 발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "nickname_01 - 해당 닉네임 중복" +
                    " \t\n member_01 - 가입하려는 사용자가 이미 서버에 존재", content = @Content(
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    public ResponseEntity<ResSignInDto> signUp(@RequestBody SignUpDto signUpDto) {

        return ResponseUtil.createResponseWithCookieAndBody(authService.signUp(signUpDto));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "사용자 회원탈퇴", description = "secure - 회원탈퇴 api, 댓글은 삭제처리만 되고, 사용자 관련 데이터는 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공")
    })
    public void deleteAccount() {

        accountService.deleteAccount();
    }
}
