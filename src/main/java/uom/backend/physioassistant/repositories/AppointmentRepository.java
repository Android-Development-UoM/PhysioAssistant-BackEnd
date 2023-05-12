package uom.backend.physioassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uom.backend.physioassistant.models.appointment.AppointmentStatus;
import uom.backend.physioassistant.models.appointment.Appointment;

import java.util.Collection;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Get all appointments for a given patient
    @Query("SELECT a FROM Appointment a WHERE a.patientId = ?1")
    Collection<Appointment> findAllByPatientId(String patientId);

    // Get all appointments for a given doctor
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = ?1")
    Collection<Appointment> findAllByDoctorId(String doctorId);

    // Get all appointment requests for a given doctor (Appointment Requests are the Appointments with a status of: "PENDING")
    @Query("SELECT a FROM Appointment a WHERE a.status = uom.backend.physioassistant.models.appointment.AppointmentStatus.PENDING and a.doctorId = ?1")
    Collection<Appointment> findAllPendingByDoctorId(String doctorId);

    // Get all appointment requests for a given patient (Appointment Requests are the Appointments with a status of: "PENDING")
    @Query("SELECT a FROM Appointment a WHERE a.status = uom.backend.physioassistant.models.appointment.AppointmentStatus.PENDING and a.patientId = ?1")
    Collection<Appointment> findAllPendingByPatientId(String patient);

    // Get all appointment requests for a given doctor (Appointment Requests are the Appointments with a status of: "PENDING")
    @Query("SELECT a FROM Appointment a WHERE a.status = uom.backend.physioassistant.models.appointment.AppointmentStatus.ACCEPTED and a.doctorId = ?1")
    Collection<Appointment> findAllAcceptedByDoctorId(String doctorId);

    // Get all appointment requests for a given patient (Appointment Requests are the Appointments with a status of: "PENDING")
    @Query("SELECT a FROM Appointment a WHERE a.status = uom.backend.physioassistant.models.appointment.AppointmentStatus.ACCEPTED and a.patientId = ?1")
    Collection<Appointment> findAllAcceptedByPatientId(String patient);
}
