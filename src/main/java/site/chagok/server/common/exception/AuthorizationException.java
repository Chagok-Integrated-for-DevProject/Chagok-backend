package site.chagok.server.common.exception;

public class AuthorizationException extends CustomException{

    public AuthorizationException(String code, String state) {
        super(401, code, state);
    }
}
