package site.chagok.server.member.exception;

import site.chagok.server.common.exception.AuthorizationApiException;

public class InvalidMemberApiException extends AuthorizationApiException {
    public InvalidMemberApiException() {
        super("invalid_01", "invalid member");
    }
}
