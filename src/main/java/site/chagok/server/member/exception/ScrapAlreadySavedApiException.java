package site.chagok.server.member.exception;

import site.chagok.server.common.exception.UpdateInfoApiException;

public class ScrapAlreadySavedApiException extends UpdateInfoApiException {
    public ScrapAlreadySavedApiException() {
        super("scrap_02", "this board has already been scrapped");
    }
}
