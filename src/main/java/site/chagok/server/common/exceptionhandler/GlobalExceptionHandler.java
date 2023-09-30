package site.chagok.server.common.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.chagok.server.common.exception.AuthorizationException;
import site.chagok.server.common.exception.NotFoundException;
import site.chagok.server.member.exception.UpdateInfoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<?> invalidMemberExceptionHandler(AuthorizationException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getErrorDto());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundExceptionHandler(NotFoundException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getErrorDto());
    }

    @ExceptionHandler(UpdateInfoException.class)
    public ResponseEntity<?> updateInfoExceptionHandler(UpdateInfoException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getErrorDto());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> illegalStateExceptionHandler(IllegalStateException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
