package site.chagok.server.member.exception;

import site.chagok.server.common.exception.CustomException;

public class RequestInfoException extends CustomException {

    public RequestInfoException(String code, String state) {
        super(400, code, state);
    }
}
