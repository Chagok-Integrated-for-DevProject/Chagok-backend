package site.chagok.server.common.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.chagok.server.common.exception.AuthorizationApiException;
import site.chagok.server.common.exception.NotFoundApiException;
import site.chagok.server.common.exception.UpdateInfoApiException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationApiException.class)
    public ResponseEntity<?> invalidMemberExceptionHandler(AuthorizationApiException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getErrorDto());
    }

    @ExceptionHandler(NotFoundApiException.class)
    public ResponseEntity<?> notFoundExceptionHandler(NotFoundApiException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getErrorDto());
    }

    @ExceptionHandler(UpdateInfoApiException.class)
    public ResponseEntity<?> updateInfoExceptionHandler(UpdateInfoApiException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getErrorDto());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> illegalStateExceptionHandler(IllegalStateException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
