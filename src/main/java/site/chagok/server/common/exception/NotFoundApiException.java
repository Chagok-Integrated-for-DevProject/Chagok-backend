package site.chagok.server.common.exception;

public class NotFoundApiException extends CustomApiException {

    public NotFoundApiException(String code, String state) {
        super(404, code, state);
    }
}
