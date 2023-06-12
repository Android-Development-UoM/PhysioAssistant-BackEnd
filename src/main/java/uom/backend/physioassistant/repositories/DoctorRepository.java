package uom.backend.physioassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.models.users.Patient;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, String> {
    Optional<Doctor> findByUsername(String username);

}
