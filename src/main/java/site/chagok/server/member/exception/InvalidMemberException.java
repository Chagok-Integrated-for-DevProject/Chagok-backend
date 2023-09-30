package site.chagok.server.member.exception;

import org.springframework.security.access.AuthorizationServiceException;

public class InvalidMemberException extends AuthorizationServiceException {
    public InvalidMemberException() {
        super("invalid member");
    }
}
