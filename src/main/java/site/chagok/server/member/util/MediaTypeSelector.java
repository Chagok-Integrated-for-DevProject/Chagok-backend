package site.chagok.server.member.util;

import org.springframework.http.MediaType;

import java.io.FileNotFoundException;

public class MediaTypeSelector {

    public static MediaType getMediaType(String fileName) throws FileNotFoundException {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        switch (extension) {
            case "jpg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            default:
                throw new FileNotFoundException();
        }
    }
}
