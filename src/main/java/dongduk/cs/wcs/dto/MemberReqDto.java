package dongduk.cs.wcs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

public class MemberReqDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientSignupReqDto {
        @Pattern(regexp = "^[a-z0-9]{2,15}$",
        message = "아이디는 영어 소문자와 숫자로 이루어진 2 ~ 15자 사이여야 합니다.")
        private String id;

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-zA-Z\\d!@#$%^&*()]{8,20}$",
        message = "비밀번호는 8 ~ 20자 이상이어야 하며, 영문 대소문자, 숫자, 특수 문자를 각각 1개 이상 포함해야 합니다.")
        private String password;

        @NotBlank(message = "이름은 필수 입력값입니다.")
        private String name;

        @NotNull(message = "생일은 필수 입력값입니다.")
        private LocalDate birthday;

        @NotNull(message = "성별은 필수 입력값입니다.")
        private Integer gender;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorSignupReqDto {
        @Pattern(regexp = "^[a-z0-9]{2,15}$",
                message = "아이디는 영어 소문자와 숫자로 이루어진 2 ~ 15자 사이여야 합니다.")
        private String id;

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-zA-Z\\d!@#$%^&*()]{8,20}$",
                message = "비밀번호는 8 ~ 20자 이상이어야 하며, 영문 대소문자, 숫자, 특수 문자를 각각 1개 이상 포함해야 합니다.")
        private String password;

        @NotBlank(message = "이름은 필수 입력값입니다.")
        private String name;

        @NotNull(message = "진료 과목은 필수 입력값입니다.")
        private Integer subject;
    }
}
