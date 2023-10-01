package site.chagok.server.member.exception;

import site.chagok.server.common.exception.NotFoundApiException;

public class ImgFileNotFoundApiException extends NotFoundApiException {

    public ImgFileNotFoundApiException() {
        super("image_01", "cannot get profile image");
    }
}
