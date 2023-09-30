package site.chagok.server.common.exception;

import site.chagok.server.common.dto.ErrorDto;

public class CustomException extends RuntimeException{

    ErrorDto errorDto;

    public CustomException(int status, String code, String state) {
        this.errorDto = new ErrorDto(status, code, state);
    }

    public ErrorDto getErrorDto() {
        return errorDto;
    }

    public int getHttpStatus() {
        return errorDto.getStatus();
    }
}
