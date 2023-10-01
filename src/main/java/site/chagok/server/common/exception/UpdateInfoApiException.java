package site.chagok.server.common.exception;


public class UpdateInfoApiException extends CustomApiException {

    public UpdateInfoApiException(String code, String state) {
        super(400, code, state);
    }
}
