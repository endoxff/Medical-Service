package dongduk.cs.wcs.domain;

import dongduk.cs.wcs.domain.enums.Subject;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DiscriminatorValue("DOCTOR")
public class Doctor extends Member {

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    private Subject subject;

    public Doctor(String id, String password, String name, byte[] publicKey, Subject subject) {
        super.setId(id);
        super.setPassword(password);
        super.setName(name);
        super.setPublicKey(publicKey);
        this.subject = subject;
    }
}
