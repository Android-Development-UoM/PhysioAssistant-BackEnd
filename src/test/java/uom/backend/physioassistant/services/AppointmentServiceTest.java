package uom.backend.physioassistant.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uom.backend.physioassistant.dtos.requests.CreateAppointmentRequest;
import uom.backend.physioassistant.models.appointment.Appointment;
import uom.backend.physioassistant.models.appointment.AppointmentStatus;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.models.users.Patient;
import uom.backend.physioassistant.repositories.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorService doctorService;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAppointmentById_ExistingAppointmentId_ReturnsAppointment() {
        // Arrange
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        // Act
        Appointment result = appointmentService.getAppointmentById(appointmentId);

        // Assert
        assertThat(result).isEqualTo(appointment);
        verify(appointmentRepository, times(1)).findById(appointmentId);
    }

    @Test
    public void testGetAppointmentById_NonExistingAppointmentId_ThrowsEntityNotFoundException() {
        // Arrange
        Long nonExistingAppointmentId = 100L;
        when(appointmentRepository.findById(nonExistingAppointmentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> appointmentService.getAppointmentById(nonExistingAppointmentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Appointment with id: " + nonExistingAppointmentId + " not found.");
        verify(appointmentRepository, times(1)).findById(nonExistingAppointmentId);
    }

    @Test
    public void testCreateAppointment_ValidRequest_ReturnsCreatedAppointment() {
        // Arrange
        CreateAppointmentRequest appointmentRequest = new CreateAppointmentRequest();
        appointmentRequest.setDoctorId("doctor1");
        appointmentRequest.setPatientId("patient1");
        appointmentRequest.setDate(LocalDate.now());
        appointmentRequest.setTime(LocalTime.now());

        Doctor doctor = new Doctor();
        when(doctorService.getById(appointmentRequest.getDoctorId())).thenReturn(doctor);

        Patient patient = new Patient();
        when(patientService.getPatientById(appointmentRequest.getPatientId())).thenReturn(patient);

        Appointment savedAppointment = new Appointment();
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

        // Act
        Appointment result = appointmentService.createAppointment(appointmentRequest);

        // Assert
        assertThat(result).isEqualTo(savedAppointment);
        verify(doctorService, times(1)).getById(appointmentRequest.getDoctorId());
        verify(patientService, times(1)).getPatientById(appointmentRequest.getPatientId());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    public void testGetAllAppointments_ReturnsAllAppointments() {
        // Arrange
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());
        when(appointmentRepository.findAll()).thenReturn(appointments);

        // Act
        Collection<Appointment> result = appointmentService.getAllAppointments();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(appointments);
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    public void testGetAppointmentsBasedOnDoctorId_ReturnsAppointmentsForDoctorId() {
        // Arrange
        String doctorId = "doctor1";
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());
        when(appointmentRepository.findAllByDoctorId(doctorId)).thenReturn(appointments);

        // Act
        Collection<Appointment> result = appointmentService.getAppointmentsBasedOnDoctorId(doctorId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(appointments);
        verify(appointmentRepository, times(1)).findAllByDoctorId(doctorId);
    }

    @Test
    public void testGetAppointmentsBasedOnPatientId_ReturnsAppointmentsForPatientId() {
        // Arrange
        String patientId = "patient1";
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());
        when(appointmentRepository.findAllByPatientId(patientId)).thenReturn(appointments);

        // Act
        Collection<Appointment> result = appointmentService.getAppointmentsBasedOnPatientId(patientId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(appointments);
        verify(appointmentRepository, times(1)).findAllByPatientId(patientId);
    }

    @Test
    public void testGetAllForDoctorByStatus_ReturnsAppointmentsForDoctorIdAndStatus() {
        // Arrange
        String doctorId = "doctor1";
        AppointmentStatus status = AppointmentStatus.ACCEPTED;
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());
        when(appointmentRepository.findAllForDoctorByStatus(doctorId, status)).thenReturn(appointments);

        // Act
        Collection<Appointment> result = appointmentService.getAllForDoctorByStatus(doctorId, status);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(appointments);
        verify(appointmentRepository, times(1)).findAllForDoctorByStatus(doctorId, status);
    }

    @Test
    public void testGetAllForPatientByStatus_ReturnsAppointmentsForPatientIdAndStatus() {
        // Arrange
        String patientId = "patient1";
        AppointmentStatus status = AppointmentStatus.ACCEPTED;
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());
        when(appointmentRepository.findAllForPatientByStatus(patientId, status)).thenReturn(appointments);

        // Act
        Collection<Appointment> result = appointmentService.getAllForPatientByStatus(patientId, status);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(appointments);
        verify(appointmentRepository, times(1)).findAllForPatientByStatus(patientId, status);
    }

    @Test
    public void testSetAppointmentStatus_ValidAppointmentIdAndStatus_StatusUpdated() {
        // Arrange
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        AppointmentStatus newStatus = AppointmentStatus.DONE;

        // Act
        appointmentService.setAppointmentStatus(appointmentId, newStatus);

        // Assert
        assertThat(appointment.getStatus()).isEqualTo(newStatus);
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    public void testSetAppointmentStatus_NonExistingAppointmentId_ThrowsEntityNotFoundException() {
        // Arrange
        Long nonExistingAppointmentId = 100L;
        when(appointmentRepository.findById(nonExistingAppointmentId)).thenReturn(Optional.empty());

        AppointmentStatus newStatus = AppointmentStatus.DONE;

        // Act and Assert
        assertThatThrownBy(() -> appointmentService.setAppointmentStatus(nonExistingAppointmentId, newStatus))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Appointment with id: " + nonExistingAppointmentId + " not found.");
        verify(appointmentRepository, times(1)).findById(nonExistingAppointmentId);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }
}

