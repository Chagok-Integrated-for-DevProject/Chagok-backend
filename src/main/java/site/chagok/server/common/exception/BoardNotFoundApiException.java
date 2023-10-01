package site.chagok.server.common.exception;

public class BoardNotFoundApiException extends NotFoundApiException {

    public BoardNotFoundApiException() {
        super("board_001", "cannot find board");
    }
}
