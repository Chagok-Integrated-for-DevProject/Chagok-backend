package site.chagok.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.repository.MemberRepository;
import site.chagok.server.member.util.MemberCredential;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImgService {

    /*
     이미지 업데이트, 조회 서비스
     */

    @Value("${profileImgLocation}")
    String basePath;

    private final MemberRepository memberRepository;


    @Transactional
    public void updateProfileImg(MultipartFile imgFile) throws IOException {


        // 시큐어코딩 - 이미지 형식만 허용
        if(!Objects.requireNonNull(imgFile.getContentType()).startsWith("image")){
            return;
        }

        String userEmail = MemberCredential.getLoggedMemberEmail();

        Member member = memberRepository.findByEmail(userEmail).orElseThrow(EntityNotFoundException::new);

        // 파일 저장
        String fileName = saveFile(Objects.requireNonNull(imgFile.getOriginalFilename()), imgFile.getBytes());

        // 설정했던 이미지 삭제
        if (member.getProfileImg() != null)
            deleteFile(member.getProfileImg());

        // 사용자 엔티티에 파일 이름 갱신
        member.updateProfileImg(fileName);
    }

    // 이미지 저장
    private String saveFile(String originalFIleName, byte[] fileData) throws IOException {

        // 확장자
        String extension = originalFIleName.substring(originalFIleName.lastIndexOf("."));
        // 랜덤 uuid
        String uuidToPath = UUID.randomUUID().toString();
        // path 생성
        String uploadFilePath = basePath + "/" + uuidToPath + extension;
        // 저장
        try (FileOutputStream fos = new FileOutputStream(uploadFilePath)) {
            fos.write(fileData);
        };
        // 파일이름 및 확장자 return
        return uuidToPath + extension;
    }

    private void deleteFile(String fileName) {
        String deleteFilePath = basePath + "/" + fileName;

        File deleteFile = new File(deleteFilePath);


        if (deleteFile.exists()) {
            deleteFile.delete();
        }
    }

    // 이미지 조회
    @Transactional
    public byte[] getProfileImg(String image) throws IOException {

        String fileName = memberRepository.findByProfileImg(image).orElseThrow(FileNotFoundException::new).getProfileImg();
        File savedFile = getFile(fileName);

        return Files.readAllBytes(savedFile.toPath());
    }

    private File getFile(String fileName) throws FileNotFoundException {
        String deleteFilePath = basePath + "/" + fileName;

        File savedFile = new File(deleteFilePath);

        if (!savedFile.exists())
            throw new FileNotFoundException();

        return savedFile;
    }

    public MediaType getMediaType(String fileName) throws FileNotFoundException {
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
