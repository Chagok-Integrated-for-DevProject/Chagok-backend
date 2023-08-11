package site.chagok.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImgService {

    /*
     이미지 업데이트 서비스
     */

    @Value("${profileImgLocation}")
    String fileLocation;
    private final FileService fileService;
    private final MemberRepository memberRepository;


    @Transactional
    public void updateFile(MultipartFile imgFile) throws IOException {


        // 시큐어코딩 이미지 형식이어야 함..
        if(!Objects.requireNonNull(imgFile.getContentType()).startsWith("image")){
            return;
        }

        String userEmail = MemberCredential.getLoggedMemberEmail();

        Member member = memberRepository.findByEmail(userEmail).orElseThrow(EntityNotFoundException::new);

        // 설정했던 이미지 삭제
        if (member.getProfileImg() != null)
            fileService.deleteFile(member.getProfileImg());

        // 파일 저장
        String fileName = fileService
                .saveFile(fileLocation, Objects.requireNonNull(imgFile.getOriginalFilename()), imgFile.getBytes());

        // 사용자 엔티티에 파일 이름 갱신
        member.updateProfileImg(fileName);
    }

}
