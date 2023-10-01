package site.chagok.server.member.exception;

import site.chagok.server.common.exception.UpdateInfoApiException;

public class NickNameExistsApiException extends UpdateInfoApiException {

    public NickNameExistsApiException() {
        super("nickname_01", "nickname already exists");
    }
}
