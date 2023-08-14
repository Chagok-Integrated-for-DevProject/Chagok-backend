package site.chagok.server.member.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardScrapDto {

    @ApiModelProperty(name = "category", value = "project or study or contest 값만 가능", example = "only, project or study or contest")
    private String category;

    @JsonProperty("id")
    @ApiModelProperty(name = "id")
    private Long boardId;
}
