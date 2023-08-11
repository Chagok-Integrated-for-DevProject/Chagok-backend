package site.chagok.server.member.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardScrap {


    @ApiModelProperty(name = "category", value = "project or study or contest 값만 가능", example = "project")
    private String category;

    @JsonProperty("id")
    @ApiModelProperty(name = "id")
    private Long boardId;
}
