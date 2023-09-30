package site.chagok.server.contest.exception;

import javax.persistence.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {

    public CommentNotFoundException() {
        super("cannot find comment");
    }
}
