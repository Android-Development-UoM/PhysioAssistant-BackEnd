package uom.backend.physioassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uom.backend.physioassistant.models.appointment.AppointmentStatus;
import uom.backend.physioassistant.models.appointment.Appointment;

import java.util.Collection;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Get all appointments for a given patient
    @Query("SELECT a FROM Appointment a WHERE a.patient.username = ?1")
    Collection<Appointment> findAllByPatientId(String patientId);

    // Get all appointments for a given doctor
    @Query("SELECT a FROM Appointment a WHERE a.doctor.username = ?1")
    Collection<Appointment> findAllByDoctorId(String doctorId);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.username = ?1 and a.status = ?2")
    Collection<Appointment> findAllForDoctorByStatus(String doctorId, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.patient.username = ?1 and a.status = ?2")
    Collection<Appointment> findAllForPatientByStatus(String patientId, AppointmentStatus status);
}
