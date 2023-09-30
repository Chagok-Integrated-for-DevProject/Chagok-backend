package site.chagok.server.security.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.*;
import site.chagok.server.security.domain.AuthInfo;
import site.chagok.server.security.dto.JwtTokenSetDto;
import site.chagok.server.security.dto.ReqSignInDto;
import site.chagok.server.security.dto.ResSignInDto;
import site.chagok.server.security.dto.SignUpDto;
import site.chagok.server.security.service.AccountService;
import site.chagok.server.security.service.AuthService;
import site.chagok.server.security.util.ResponseUtil;

import javax.persistence.EntityNotFoundException;

@Api(tags = "인증 및 회원가입/탈퇴")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final AuthService authService;
    private final AccountService accountService;

    @PostMapping("/signIn")
    @ApiOperation(value = "로그인", notes = "가입이 되지 않은 상태라면, isSignUp 이 false, 가입이 되어있는 상태라면, isSignUp이 true 및 jwt, refresh 토큰(쿠키:refreshToken - httpOnly, secure 적용) 발급")
    @ApiResponses({@ApiResponse(code = 200, message = "서버 인증 성공"), @ApiResponse(code = 400, message = "사용자 정보 획득 에러(oauth2.0 통신과정) 또는 access code 에러")})
    public ResponseEntity<ResSignInDto> signIn(@RequestBody ReqSignInDto reqSignInDto) {

        AuthInfo authInfo = authService.signIn(reqSignInDto);

        if (authInfo.isSignUp()) // 가입이 된 상태라서, 바로 로그인 완료 및 jwt 발급
            return ResponseUtil.createResponseWithCookieAndBody(authService.signIn(reqSignInDto));

        return ResponseEntity.ok().body(authInfo.getSignInResDto()); // 가입이 되지 않은 상태
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "secure - 리프레시 토큰 전달")
    @ApiResponses({@ApiResponse(code = 200, message = "리프레시 토큰 갱신 성공"), @ApiResponse(code = 400, message = "refresh token 에러 또는 access token 에러")})
    public ResponseEntity<ResSignInDto> renewRefreshTokenAuth(@CookieValue("refreshToken") String refreshToken, @RequestBody String jwtToken) {

        return ResponseUtil.createResponseWithCookieAndBody(authService.refresh(new JwtTokenSetDto(jwtToken, refreshToken)));
    }

    @PostMapping("/signUp")
    @ApiOperation(value = "사용자 회원가입", notes = "회원가입 성공 시, jwt, refresh 토큰(쿠키:refreshToken - httpOnly, secure 적용) 발급")
    @ApiResponses({@ApiResponse(code = 200, message = "회원가입 성공"), @ApiResponse(code = 400, message = "사용자 정보 획득 에러(oauth2.0 통신과정) 또는 access code 에러, 이미 사용자가 존재하거나 닉네임이 존재한다면 에러")})
    public ResponseEntity<ResSignInDto> signUp(@RequestBody SignUpDto signUpDto) {

        return ResponseUtil.createResponseWithCookieAndBody(authService.signUp(signUpDto));
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "사용자 회원탈퇴", notes = "secure - 회원탈퇴 api, 댓글은 삭제처리만 되고, 사용자 관련 데이터는 삭제")
    @ApiResponses({@ApiResponse(code = 200, message = "회원탈퇴 성공"), @ApiResponse(code = 400, message = "회원탈퇴 에러")})
    public void deleteAccount() {

        accountService.deleteAccount();
    }
}
