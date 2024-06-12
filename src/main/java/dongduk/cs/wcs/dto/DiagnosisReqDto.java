package dongduk.cs.wcs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class DiagnosisReqDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiagnosisRequestReqDto {
        @NotNull(message = "의사를 선택해주세요.")
        private String receiver;

        @NotBlank(message = "증상을 입력해주세요.")
        private String symptom;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiagnosisResponseReqDto {
        @NotNull(message = "환자를 선택해주세요.")
        private String receiver;

        @NotBlank(message = "증상을 입력해주세요.")
        private String prescription;
    }
}
