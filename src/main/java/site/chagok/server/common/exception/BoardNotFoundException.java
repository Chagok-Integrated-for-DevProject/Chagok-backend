package site.chagok.server.common.exception;

import javax.persistence.EntityNotFoundException;

public class BoardNotFoundException extends NotFoundException {

    public BoardNotFoundException() {
        super("board_001", "cannot find board");
    }
}
