package uom.backend.physioassistant.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.exceptions.NotFoundException;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.repositories.DoctorRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDoctors_ShouldReturnListOfDoctors() {
        // Arrange
        Doctor doctor1 = new Doctor();
        Doctor doctor2 = new Doctor();
        Collection<Doctor> expectedDoctors = new ArrayList<>();
        expectedDoctors.add(doctor1);
        expectedDoctors.add(doctor2);
        when(doctorRepository.findAll()).thenReturn((List<Doctor>) expectedDoctors);

        // Act
        Collection<Doctor> actualDoctors = doctorService.getAllDoctors();

        // Assert
        assertThat(actualDoctors).isEqualTo(expectedDoctors);
        verify(doctorRepository).findAll();
    }

    @Test
    void getById_ExistingId_ShouldReturnDoctor() {
        // Arrange
        String id = "1";
        Doctor expectedDoctor = new Doctor();
        when(doctorRepository.findById(id)).thenReturn(Optional.of(expectedDoctor));

        // Act
        Doctor actualDoctor = doctorService.getById(id);

        // Assert
        assertThat(actualDoctor).isEqualTo(expectedDoctor);
        verify(doctorRepository).findById(id);
    }

    @Test
    void getById_NonexistentId_ShouldThrowEntityNotFoundException() {
        // Arrange
        String id = "1";
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> doctorService.getById(id));
        verify(doctorRepository).findById(id);
    }

    @Test
    void getOptionalById_ExistingId_ShouldReturnOptionalWithDoctor() {
        // Arrange
        String id = "1";
        Doctor expectedDoctor = new Doctor();
        when(doctorRepository.findById(id)).thenReturn(Optional.of(expectedDoctor));

        // Act
        Optional<Doctor> actualDoctor = doctorService.getOptionalById(id);

        // Assert
        assertThat(actualDoctor).isPresent().containsSame(expectedDoctor);
        verify(doctorRepository).findById(id);
    }

    @Test
    void getOptionalById_NonexistentId_ShouldReturnEmptyOptional() {
        // Arrange
        String id = "1";
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Doctor> actualDoctor = doctorService.getOptionalById(id);

        // Assert
        assertThat(actualDoctor).isEmpty();
        verify(doctorRepository).findById(id);
    }

    @Test
    void deleteById_ExistingId_ShouldDeleteDoctor() {
        // Arrange
        String id = "1";
        Doctor expectedDoctor = new Doctor();
        when(doctorRepository.findById(id)).thenReturn(Optional.of(expectedDoctor));

        // Act
        doctorService.deleteById(id);

        // Assert
        verify(doctorRepository).findById(id);
        verify(doctorRepository).delete(expectedDoctor);
    }

    @Test
    void deleteById_NonexistentId_ShouldThrowEntityNotFoundException() {
        // Arrange
        String id = "1";
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> doctorService.deleteById(id));
        verify(doctorRepository).findById(id);
        verify(doctorRepository, never()).delete(any());
    }

    @Test
    void createDoctor_NewDoctor_ShouldSaveDoctor() {
        // Arrange
        Doctor doctor = new Doctor();
        String givenAFM = "123456789";
        doctor.setUsername(givenAFM);
        when(doctorRepository.findById(givenAFM)).thenReturn(Optional.empty());
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        // Act
        Doctor createdDoctor = doctorService.createDoctor(doctor);

        // Assert
        assertThat(createdDoctor).isEqualTo(doctor);
        verify(doctorRepository).findById(givenAFM);
        verify(doctorRepository).save(doctor);
    }

    @Test
    void createDoctor_DoctorAlreadyExists_ShouldThrowAlreadyAddedException() {
        // Arrange
        Doctor doctor = new Doctor();
        String givenAFM = "123456789";
        doctor.setUsername(givenAFM);
        when(doctorRepository.findById(givenAFM)).thenReturn(Optional.of(doctor));

        // Act and Assert
        assertThatExceptionOfType(AlreadyAddedException.class)
                .isThrownBy(() -> doctorService.createDoctor(doctor));
        verify(doctorRepository).findById(givenAFM);
        verify(doctorRepository, never()).save(any());
    }
}
