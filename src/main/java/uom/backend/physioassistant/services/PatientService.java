package uom.backend.physioassistant.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import uom.backend.physioassistant.dtos.requests.CreatePatientRequest;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.models.users.Patient;
import uom.backend.physioassistant.repositories.PatientRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;

    public PatientService(PatientRepository patientRepository, DoctorService doctorService) {
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
    }

    public Collection<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Collection<Patient> getAllPatientsByDoctorId(String doctorId) {
        return patientRepository.getAllByDoctorId(doctorId);
    }

    public Patient getPatientById(String id) {
        Optional<Patient> foundPatient = patientRepository.findById(id);

        if (foundPatient.isEmpty())
            throw new EntityNotFoundException("Patient with id: " + id + " not found.");

        return foundPatient.get();
    }

    public Patient createPatient(CreatePatientRequest patientRequest) {
        // Make sure the patient is not already added
        String givenAMKA = patientRequest.getAmka();
        Optional<Patient> foundPatient = patientRepository.findById(givenAMKA);

        if (foundPatient.isPresent())
            throw new AlreadyAddedException("Patient with AMKA: " + givenAMKA + " is already added.");

        String givenName = patientRequest.getName();
        String givenAddress = patientRequest.getAddress();
        String givenPassword = patientRequest.getPassword();
        String doctorId = patientRequest.getDoctorId();

        Doctor doctor = doctorService.getById(doctorId);

        Patient patient = new Patient();
        patient.setAmka(givenAMKA);
        patient.setName(givenName);
        patient.setDoctor(doctor);
        patient.setAddress(givenAddress);
        patient.setPassword(givenPassword);

        return patientRepository.save(patient);
    }

    public void deletePatientById(String id) {
        Patient patient = this.getPatientById(id);
        patientRepository.delete(patient);
    }
}
