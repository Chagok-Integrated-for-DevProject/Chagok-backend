package site.chagok.server.member.exception;

import site.chagok.server.common.exception.NotFoundException;

public class ImgFileNotFoundException extends NotFoundException {

    public ImgFileNotFoundException() {
        super("image_01", "cannot get profile image");
    }
}
