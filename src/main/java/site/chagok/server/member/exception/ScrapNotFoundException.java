package site.chagok.server.member.exception;

import javax.persistence.EntityNotFoundException;

public class ScrapNotFoundException extends EntityNotFoundException {

    public ScrapNotFoundException() {
        super("cannot find board scrap data");
    }
}
