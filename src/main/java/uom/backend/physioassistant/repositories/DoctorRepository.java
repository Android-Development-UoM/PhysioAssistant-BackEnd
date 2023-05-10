package uom.backend.physioassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uom.backend.physioassistant.models.users.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, String> {
}
