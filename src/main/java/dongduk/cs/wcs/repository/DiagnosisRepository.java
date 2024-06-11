package dongduk.cs.wcs.repository;

import dongduk.cs.wcs.domain.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    public List<Diagnosis> findByReceiverId(String receiverId);
}
