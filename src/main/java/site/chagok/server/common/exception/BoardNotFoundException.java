package site.chagok.server.common.exception;

import javax.persistence.EntityNotFoundException;

public class BoardNotFoundException extends EntityNotFoundException {

    public BoardNotFoundException() {
        super("cannot find board");
    }
}
