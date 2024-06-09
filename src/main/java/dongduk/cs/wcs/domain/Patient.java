package dongduk.cs.wcs.domain;

import dongduk.cs.wcs.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DiscriminatorValue("PATIENT")
public class Patient extends Member {
    @Column(nullable = false)
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    private Gender gender;

    public Patient(String id, String password, String name, byte[] publicKey,
                   LocalDate birthday, Gender gender) {
        super.setId(id);
        super.setPassword(password);
        super.setName(name);
        super.setPublicKey(publicKey);
        this.birthday = birthday;
        this.gender = gender;
    }
}
