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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.chagok.server.security.dto.SignInRequestDto;
import site.chagok.server.security.dto.SignInResponseDto;
import site.chagok.server.security.service.AuthService;

@Api(tags = "인증")
@RestController
@RequiredArgsConstructor
public class SecurityController {

    private final AuthService authService;

    @PostMapping("/auth")
    @ApiOperation(value = "액세스 토큰 전달")
    @ApiResponses({@ApiResponse(code = 200, message = "서버 인증 성공"), @ApiResponse(code = 400, message = "사용자 json 파싱 에러 또는 access code 에러")})
    public ResponseEntity<SignInResponseDto> accessTokenEndPoint(@RequestBody SignInRequestDto signInRequestDto){

        try {
            return ResponseEntity.ok().body(authService.signIn(signInRequestDto));
        } catch (JsonProcessingException e) {
            return new ResponseEntity("cannot get data", HttpStatus.BAD_REQUEST);
        } catch(AuthorizationServiceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
