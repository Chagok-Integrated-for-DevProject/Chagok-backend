package site.chagok.server.member.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
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
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImgService {

    /*
     이미지 업데이트, 조회 서비스
     */

    private final MemberRepository memberRepository;
    private final FireBaseService fireBaseService;


    @Transactional
    public void updateProfileImg(MultipartFile imgFile) throws IOException {


        // 시큐어코딩 - 이미지 형식만 허용
        if(!Objects.requireNonNull(imgFile.getContentType()).startsWith("image")){
            throw new FileUploadException();
        }

        String userEmail = MemberCredential.getLoggedMemberEmail();

        Member member = memberRepository.findByEmail(userEmail).orElseThrow(EntityNotFoundException::new);

        // 파일 저장
        String fileName = fireBaseService.saveImage(imgFile);

        // 설정했던 이미지 삭제
        if (member.getProfileImg() != null)
            fireBaseService.deleteImg(member.getProfileImg());

        // 사용자 엔티티에 파일 이름 갱신
        member.updateProfileImg(fileName);
    }

    // 이미지 조회
    @Transactional
    public byte[] getProfileImg(String image) throws IOException {

        String fileName = memberRepository.findByProfileImg(image).orElseThrow(FileNotFoundException::new).getProfileImg();
        byte[] savedFile = fireBaseService.getImg(fileName);

        return savedFile;
    }
}
