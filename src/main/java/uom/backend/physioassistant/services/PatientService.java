package uom.backend.physioassistant.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.exceptions.NotFoundException;
import uom.backend.physioassistant.models.users.Patient;
import uom.backend.physioassistant.repositories.PatientRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Collection<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(String id) {
        Optional<Patient> foundPatient = patientRepository.findById(id);

        if (foundPatient.isEmpty())
            throw new EntityNotFoundException("Patient with id: " + id + " not found.");

        return foundPatient.get();
    }

    public Patient createPatient(Patient patient) {
        String givenAMKA = patient.getAmka();
        Optional<Patient> foundPatient = patientRepository.findById(givenAMKA);

        if (foundPatient.isPresent())
            throw new AlreadyAddedException("Patient with AMKA: " + givenAMKA + " is already added.");

        return patientRepository.save(patient);
    }

    public void deletePatientById(String id) {
        Patient patient = this.getPatientById(id);
        patientRepository.delete(patient);
    }
}
