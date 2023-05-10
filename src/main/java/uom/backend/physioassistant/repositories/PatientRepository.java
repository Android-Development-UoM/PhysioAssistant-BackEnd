package uom.backend.physioassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uom.backend.physioassistant.models.users.Patient;

public interface PatientRepository extends JpaRepository<Patient, String> {
}
