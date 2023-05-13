package uom.backend.physioassistant.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import uom.backend.physioassistant.models.appointment.Appointment;
import uom.backend.physioassistant.models.appointment.AppointmentStatus;
import uom.backend.physioassistant.repositories.AppointmentRepository;

import java.util.Collection;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment getAppointmentById(Long id) {
        return this.appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment with id: " + id + " not found."));
    }

    public Appointment createAppointment(Appointment appointment) {
        return this.appointmentRepository.save(appointment);
    }

    public Collection<Appointment> getAllAppointments() {
        return this.appointmentRepository.findAll();
    }

    public Collection<Appointment> getAppointmentsBasedOnDoctorId(String doctorId) {
        return this.appointmentRepository.findAllByDoctorId(doctorId);
    }

    public Collection<Appointment> getAppointmentsBasedOnPatientId(String patientId) {
        return this.appointmentRepository.findAllByPatientId(patientId);
    }

    public Collection<Appointment> getAllForDoctorByStatus(String doctorId, AppointmentStatus status) {
        return this.appointmentRepository.findAllForDoctorByStatus(doctorId, status);
    }

    public Collection<Appointment> getAllForPatientByStatus(String patientId, AppointmentStatus status) {
        return this.appointmentRepository.findAllForPatientByStatus(patientId, status);
    }

    public void setAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = this.getAppointmentById(appointmentId);
        appointment.setStatus(status);
        this.appointmentRepository.save(appointment);
    }
}
