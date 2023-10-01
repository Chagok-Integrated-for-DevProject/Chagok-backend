package site.chagok.server.member.exception;

import site.chagok.server.common.exception.NotFoundApiException;

public class MemberNotFoundApiException extends NotFoundApiException {

    public MemberNotFoundApiException() {
        super("member_01", "cannot find member");
    }
}
