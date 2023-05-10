package uom.backend.physioassistant.services;

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

    public Optional<Patient> getPatientById(String id) {
        return patientRepository.findById(id);
    }

    public Patient createPatient(Patient patient) {
        String givenAMKA = patient.getAmka();
        Optional<Patient> foundPatient = patientRepository.findById(givenAMKA);

        if (foundPatient.isPresent())
            throw new AlreadyAddedException("Patient with AMKA: " + givenAMKA + " is already added.");

        return patientRepository.save(patient);
    }

    public void deletePatientById(String id) {
        Optional<Patient> patient = this.getPatientById(id);

        if (patient.isEmpty())
            throw new NotFoundException("Patient with id: " + id + " not found.");

        patientRepository.delete(patient.get());
    }
}
