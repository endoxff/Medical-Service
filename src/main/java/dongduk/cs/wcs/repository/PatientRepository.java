package dongduk.cs.wcs.repository;

import dongduk.cs.wcs.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
