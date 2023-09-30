package site.chagok.server.contest.exception;

import site.chagok.server.common.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;

public class CommentNotFoundException extends NotFoundException {

    public CommentNotFoundException() {
        super("comment_01", "cannot find comment");
    }
}
