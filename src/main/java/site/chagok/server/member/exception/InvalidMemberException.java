package site.chagok.server.member.exception;

import site.chagok.server.common.exception.AuthorizationException;

public class InvalidMemberException extends AuthorizationException {
    public InvalidMemberException() {
        super("invalid_01", "invalid member");
    }
}
