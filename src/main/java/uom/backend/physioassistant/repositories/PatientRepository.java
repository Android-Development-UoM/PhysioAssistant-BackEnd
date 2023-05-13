package uom.backend.physioassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uom.backend.physioassistant.models.users.Patient;

import java.util.Collection;

public interface PatientRepository extends JpaRepository<Patient, String> {
    // Get Patients by Doctor ID
    @Query("SELECT p FROM Patient p WHERE p.doctor.afm = ?1")
    Collection<Patient> getAllByDoctorId(String doctorId);
}
