package uom.backend.physioassistant.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uom.backend.physioassistant.dtos.requests.CreatePatientRequest;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.models.users.Patient;
import uom.backend.physioassistant.repositories.PatientRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPatients_ShouldReturnListOfPatients() {
        // Arrange
        Patient patient1 = new Patient();
        Patient patient2 = new Patient();
        Collection<Patient> expectedPatients = new ArrayList<>();
        expectedPatients.add(patient1);
        expectedPatients.add(patient2);
        when(patientRepository.findAll()).thenReturn((List<Patient>) expectedPatients);

        // Act
        Collection<Patient> actualPatients = patientService.getAllPatients();

        // Assert
        assertThat(actualPatients).isEqualTo(expectedPatients);
        verify(patientRepository).findAll();
    }

    @Test
    void getAllPatientsByDoctorId_ValidDoctorId_ShouldReturnListOfPatients() {
        // Arrange
        String doctorId = "1";
        Patient patient1 = new Patient();
        Patient patient2 = new Patient();
        Collection<Patient> expectedPatients = new ArrayList<>();
        expectedPatients.add(patient1);
        expectedPatients.add(patient2);
        when(patientRepository.getAllByDoctorId(doctorId)).thenReturn(expectedPatients);

        // Act
        Collection<Patient> actualPatients = patientService.getAllPatientsByDoctorId(doctorId);

        // Assert
        assertThat(actualPatients).isEqualTo(expectedPatients);
        verify(patientRepository).getAllByDoctorId(doctorId);
    }

    @Test
    void getPatientById_ExistingId_ShouldReturnPatient() {
        // Arrange
        String id = "1";
        Patient expectedPatient = new Patient();
        when(patientRepository.findById(id)).thenReturn(Optional.of(expectedPatient));

        // Act
        Patient actualPatient = patientService.getPatientById(id);

        // Assert
        assertThat(actualPatient).isEqualTo(expectedPatient);
        verify(patientRepository).findById(id);
    }

    @Test
    void getPatientById_NonexistentId_ShouldThrowEntityNotFoundException() {
        // Arrange
        String id = "1";
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> patientService.getPatientById(id));
        verify(patientRepository).findById(id);
    }

    @Test
    void getDoctorPatientsByAmka_ValidDoctorIdAndAmka_ShouldReturnListOfPatients() {
        // Arrange
        String doctorId = "1";
        String amka = "123456789";
        Patient patient1 = new Patient();
        Patient patient2 = new Patient();
        Collection<Patient> expectedPatients = new ArrayList<>();
        expectedPatients.add(patient1);
        expectedPatients.add(patient2);
        when(patientRepository.getAllByDoctorIdAndPatientUsername(doctorId, amka)).thenReturn(expectedPatients);

        // Act
        Collection<Patient> actualPatients = patientService.getDoctorPatientsByUsername(doctorId, amka);

        // Assert
        assertThat(actualPatients).isEqualTo(expectedPatients);
        verify(patientRepository).getAllByDoctorIdAndPatientUsername(doctorId, amka);
    }

    @Test
    void createPatient_NewPatient_ShouldSavePatient() {
        // Arrange
        CreatePatientRequest patientRequest = new CreatePatientRequest();
        patientRequest.setUsername("123456789");
        patientRequest.setName("John Doe");
        patientRequest.setAddress("123 Main St");
        patientRequest.setPassword("password");
        patientRequest.setDoctorId("1");

        Doctor doctor = new Doctor();
        String doctorId = patientRequest.getDoctorId();
        when(doctorService.getById(doctorId)).thenReturn(doctor);

        Patient patient = new Patient();
        patient.setAmka(patientRequest.getUsername());
        patient.setName(patientRequest.getName());
        patient.setDoctor(doctor);
        patient.setAddress(patientRequest.getAddress());
        patient.setUsername(patientRequest.getUsername());
        patient.setPassword(patientRequest.getPassword());
        when(patientRepository.findById(patientRequest.getUsername())).thenReturn(Optional.empty());
        when(patientRepository.save(patient)).thenReturn(patient);

        // Act
        Patient createdPatient = patientService.createPatient(patientRequest);
        System.out.println(patientService.createPatient(patientRequest));

        // Assert
        assertThat(createdPatient).isEqualTo(patient);
        verify(doctorService).getById(doctorId);
        verify(patientRepository).findById(patientRequest.getUsername());
        verify(patientRepository).save(patient);
    }

    @Test
    void createPatient_PatientAlreadyExists_ShouldThrowAlreadyAddedException() {
        // Arrange
        CreatePatientRequest patientRequest = new CreatePatientRequest();
        patientRequest.setUsername("123456789");
        when(patientRepository.findById(patientRequest.getUsername())).thenReturn(Optional.of(new Patient()));

        // Act and Assert
        assertThatExceptionOfType(AlreadyAddedException.class)
                .isThrownBy(() -> patientService.createPatient(patientRequest));
        verify(patientRepository).findById(patientRequest.getUsername());
        verify(patientRepository, never()).save(any());
    }

    @Test
    void deletePatientById_ExistingId_ShouldDeletePatient() {
        // Arrange
        String id = "1";
        Patient expectedPatient = new Patient();
        when(patientRepository.findById(id)).thenReturn(Optional.of(expectedPatient));

        // Act
        patientService.deletePatientById(id);

        // Assert
        verify(patientRepository).findById(id);
        verify(patientRepository).delete(expectedPatient);
    }

    @Test
    void deletePatientById_NonexistentId_ShouldThrowEntityNotFoundException() {
        // Arrange
        String id = "1";
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> patientService.deletePatientById(id));
        verify(patientRepository).findById(id);
        verify(patientRepository, never()).delete(any());
    }
}
