package uom.backend.physioassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uom.backend.physioassistant.models.users.Patient;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, String> {
    @Query("SELECT p.username FROM Patient p")
    List<String> getAllUsernames();

    @Query("SELECT p FROM Patient p WHERE p.username = ?1")
    Optional<Patient> findByUsername(String username);
}
