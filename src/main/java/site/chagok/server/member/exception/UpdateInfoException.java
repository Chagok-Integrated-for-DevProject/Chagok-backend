package site.chagok.server.member.exception;

import site.chagok.server.common.exception.CustomException;

public class UpdateInfoException extends CustomException {

    public UpdateInfoException(String code, String state) {
        super(400, code, state);
    }
}
