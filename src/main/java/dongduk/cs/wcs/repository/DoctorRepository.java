package dongduk.cs.wcs.repository;

import dongduk.cs.wcs.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
