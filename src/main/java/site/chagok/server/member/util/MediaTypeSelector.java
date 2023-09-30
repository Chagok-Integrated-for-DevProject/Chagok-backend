package site.chagok.server.member.util;

import org.springframework.http.MediaType;
import site.chagok.server.member.exception.ImgFileNotFoundException;


public class MediaTypeSelector {

    public static MediaType getMediaType(String fileName)  {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        switch (extension) {
            case "jpg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            default:
                throw new ImgFileNotFoundException();
        }
    }
}
