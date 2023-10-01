package site.chagok.server.member.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.chagok.server.member.exception.ImgFileNotFoundApiException;
import site.chagok.server.common.exception.UpdateInfoApiException;
import site.chagok.server.member.util.MediaTypeSelector;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Service
public class FireBaseService {

    @Value("${firebase.storage.bucket}")
    private String chagokBucket;

    @Value("${firebase.config-file}")
    private String fireBaseConfig;


    // firebase 초기화
    @PostConstruct
    public void init() throws IOException {

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(fireBaseConfig).getInputStream()))
                .setStorageBucket(chagokBucket)
                .build();

        FirebaseApp.initializeApp(options);
    }

    // firebase storage image 저장
    public String saveImage(MultipartFile imgFile) {

        String originalFileName = imgFile.getOriginalFilename();

        // 확장자
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 랜덤 uuid
        String uuidToPath = UUID.randomUUID().toString();
        // path 생성
        String uploadFileName = uuidToPath + extension;

        byte[] imageData;
        try {
            imageData = imgFile.getBytes();
        } catch (IOException e) {
            throw new UpdateInfoApiException("image_01", "image update error");
        }

        // firebase storage bucket
        Bucket bucket = StorageClient.getInstance().bucket();
        // save
        bucket.create(uploadFileName, imageData, MediaTypeSelector.getMediaType(uploadFileName).toString());

        return uploadFileName;
    }

    // firebase storage image 조회
    public byte[] getImage(String imageFileName) {
        Bucket bucket = StorageClient.getInstance().bucket();

        Blob blob = bucket.get(imageFileName);

        if (blob == null)
            throw new ImgFileNotFoundApiException();

        return blob.getContent();
    }

    // firebase storage image 삭제
    public void deleteImage(String imageFileName) {
        Bucket bucket = StorageClient.getInstance().bucket();

        Blob blob = bucket.get(imageFileName);

        if (blob == null)
            return;
        blob.delete();
    }
}
