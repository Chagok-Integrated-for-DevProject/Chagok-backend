package site.chagok.server.contest.exception;

import site.chagok.server.common.exception.NotFoundApiException;

public class CommentNotFoundApiException extends NotFoundApiException {

    public CommentNotFoundApiException() {
        super("comment_01", "cannot find comment");
    }
}
