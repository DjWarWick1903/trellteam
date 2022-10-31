package ro.dev.trellteam.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String status;
    private ErrorDetail error = new ErrorDetail();

    @Data
    public class ErrorDetail {
        private String code;
        private String description;
        private LocalDateTime timestamp;
        private String info;
        private String developerMessage;
    }
}
