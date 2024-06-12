package dongduk.cs.wcs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DiagnosisResDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiagnosisRequestResDto {
        private Long id;
        private String sender;
        private String message;
        private boolean verify;
        private LocalDateTime createdAt;
    }
}
