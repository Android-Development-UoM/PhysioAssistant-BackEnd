package uom.backend.physioassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uom.backend.physioassistant.models.appointment.Appointment;

import java.util.Collection;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Get all appointments for a given patient
    @Query("SELECT a FROM Appointment a WHERE a.patientId = ?1")
    Collection<Appointment> findAllByPatientId(String patientId);

    // Get all appointments for a given doctor
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = ?1")
    Collection<Appointment> findAllByDoctorId(String doctorId);
}
