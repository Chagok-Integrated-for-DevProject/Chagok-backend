package site.chagok.server.member.exception;

import site.chagok.server.common.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;

public class ScrapNotFoundException extends NotFoundException {

    public ScrapNotFoundException() {
        super("scrap_01", "cannot find board scrap data");
    }
}
