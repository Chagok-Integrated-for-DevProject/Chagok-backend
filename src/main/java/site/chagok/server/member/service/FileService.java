package site.chagok.server.member.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    /*
     파일 저장, 삭제 Service
     */


    // 저장은 basePath/(RandomUUID)
    public String saveFile(String basePath, String originalFIleName, byte[] fileData) throws IOException {

        // 확장자
        String extension = originalFIleName.substring(originalFIleName.lastIndexOf("."));
        // 랜덤 uuid
        String uuidToPath = UUID.randomUUID().toString();
        // path 생성
        String uploadFilePath = basePath + "/" + uuidToPath + "." + extension;
        // 저장
        FileOutputStream fos = new FileOutputStream(uploadFilePath);
        fos.write(fileData);

        // 파일이름.확장자 반환
        return extension + "." + uploadFilePath;
    }

    // 파일삭제
    public void deleteFile(String filePath) {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
        }
    }
}
