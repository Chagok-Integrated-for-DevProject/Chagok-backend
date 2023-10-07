package site.chagok.server.member.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "게시글 스크랩 정보 - 게시글 category 와 해당 게시글 ID 를 가진 DTO")
public class BoardScrapDto {

    @Schema(description = "project or study or contest 값만 가능", example = "only, project or study or contest")
    private String category;

    @JsonProperty("id")
    @Schema(description = "스크랩 하는 게시글 id", example = "230")
    private Long boardId;
}
