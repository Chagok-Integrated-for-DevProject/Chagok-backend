package site.chagok.server.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ErrorDto {
    private int status;
    private String code;
    private String state;
}
