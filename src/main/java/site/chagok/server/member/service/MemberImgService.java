package site.chagok.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.chagok.server.member.domain.Member;
import site.chagok.server.member.exception.ImgFileNotFoundApiException;
import site.chagok.server.common.exception.UpdateInfoApiException;
import site.chagok.server.member.repository.MemberRepository;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberImgService {

    /*
     이미지 업데이트, 조회 서비스
     */

    private final MemberRepository memberRepository;
    private final FireBaseService fireBaseService;
    private final MemberCredentialService credentialService;


    @Transactional
    public void updateProfileImg(MultipartFile imgFile) throws IOException {


        // 시큐어코딩 - 이미지 형식만 허용
        if(!imgFile.getContentType().startsWith("image")){
            throw new UpdateInfoApiException("image_01", "cannot get appropriate profile image file");
        }

        Member member = credentialService.getMember();

        // 파일 저장
        String fileName = fireBaseService.saveImage(imgFile);

        // 설정했던 이미지 삭제
        if (member.getProfileImg() != null)
            fireBaseService.deleteImage(member.getProfileImg());

        // 사용자 엔티티에 파일 이름 갱신
        member.updateProfileImg(fileName);
    }

    @Transactional
    public void deleteProfileImg() {

        Member member = credentialService.getMember();

        // 설정했던 이미지 삭제
        if (member.getProfileImg() != null)
            fireBaseService.deleteImage(member.getProfileImg());

        member.updateProfileImg(null);
    }

    // 이미지 조회
    @Transactional
    public byte[] getProfileImg(String image) {

        String fileName = memberRepository.findByProfileImg(image).orElseThrow(ImgFileNotFoundApiException::new).getProfileImg();
        byte[] savedFile = fireBaseService.getImage(fileName);

        return savedFile;
    }
}
