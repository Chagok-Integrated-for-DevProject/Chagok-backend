package site.chagok.server.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "에러 응답에 대한 ErrorDto")
public class ErrorDto {

    @Schema(description = "http status", example = "400 or 401 or 404")
    private int status;
    @Schema(description = "error code", example = "에러 코드(노션에 명시)")
    private String code;
    @Schema(description = "error status", example = "해당 에러에 대한 설명")
    private String state;
}
