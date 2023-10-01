package site.chagok.server.member.exception;

import site.chagok.server.common.exception.NotFoundApiException;

public class ScrapNotFoundApiException extends NotFoundApiException {

    public ScrapNotFoundApiException() {
        super("scrap_01", "cannot find board scrap data");
    }
}
