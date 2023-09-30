package site.chagok.server.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException{

    public NotFoundException(String code, String state) {
        super(404, code, state);
    }
}
