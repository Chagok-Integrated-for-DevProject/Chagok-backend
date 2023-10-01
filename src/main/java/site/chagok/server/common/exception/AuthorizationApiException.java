package site.chagok.server.common.exception;

public class AuthorizationApiException extends CustomApiException {

    public AuthorizationApiException(String code, String state) {
        super(401, code, state);
    }
}
