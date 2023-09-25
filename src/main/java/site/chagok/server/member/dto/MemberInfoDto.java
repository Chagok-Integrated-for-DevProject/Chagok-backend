package site.chagok.server.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import site.chagok.server.common.contstans.SocialType;
import site.chagok.server.contest.dto.GetContestPreviewDto;
import site.chagok.server.project.dto.GetProjectPreviewDto;
import site.chagok.server.study.dto.GetStudyPreviewDto;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "MemberInfoDto", description = "마이페이지 사용자 정보 - 닉네임 , 프로필 이미지(조회 가능한 파일이름) , " +
        "소셜로그인(Google or Kakao) , 사용자 이메일 , 사용자 기술스택 , 사용자가 스크랩한 공모전, 스크랩, 프로젝트에 대한 미리보기 정보")
public class MemberInfoDto {

    @ApiModelProperty(notes = "사용자 닉네임", example = "apple123")
    private String nickName;

    @ApiModelProperty(notes = "사용자 프로필 이미지", example = "abcd.jpg or abcd.png")
    private String profileImg;
    @ApiModelProperty(notes = "사용자 소셜 로그인 종류", example = "Google or Kakao")
    private SocialType social;
    @ApiModelProperty(notes = "사용자 이메일", example = "apple123@gmail.com")
    private String email;
    @ApiModelProperty(notes = "사용자 기술스택")
    @JsonProperty("skills")
    private Set<String> techStacks;

    @ApiModelProperty(notes = "사용자가 스크랩한 공모전 미리보기 정보들")
    private List<GetContestPreviewDto> contestScraps;
    @ApiModelProperty(notes = "사용자가 스크랩한 스터디 미리보기 정보들")
    private List<GetStudyPreviewDto> studyScraps;
    @ApiModelProperty(notes = "사용자가 스크랩한 프로젝트 미리보기 정보들")
    private List<GetProjectPreviewDto> projectScraps;
}
