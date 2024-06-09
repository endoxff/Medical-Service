package dongduk.cs.wcs.domain;

import dongduk.cs.wcs.domain.enums.Type;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Diagnosis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    private Type type;

    @Column(nullable = false)
    private byte[] data;

    @Column(nullable = false)
    private byte[] signature;

    @Column(nullable = false)
    private byte[] secretKey;

    private LocalDateTime createdAt;
}
