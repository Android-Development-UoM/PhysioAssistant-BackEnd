package uom.backend.physioassistant.services;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import uom.backend.physioassistant.dtos.requests.CreatePatientRequest;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.models.users.Patient;
import uom.backend.physioassistant.repositories.PatientRepository;
import uom.backend.physioassistant.utils.AccountGenerator;

import java.util.ArrayList;
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


    public Patient getPatientByUsername(String username){
        Optional<Patient> foundPatient = patientRepository.findByUsername(username);
        if(foundPatient.isEmpty()){
            throw new EntityNotFoundException("Patient with username: "+username+" not found");
        }

        return foundPatient.get();

    }

    public Patient createPatient(CreatePatientRequest patientRequest) {
        // Make sure the patient is not already added
        String givenId = patientRequest.getAmka();
        Optional<Patient> foundPatient = patientRepository.findById(givenId);

        String givenAMKA = patientRequest.getAmka();
        Optional<Patient> foundPatientOptional = patientRepository.findById(givenAMKA);


        if (foundPatientOptional.isPresent())
            throw new EntityExistsException("Patient with AMKA: " + givenId + " is already added.");

        String givenName = patientRequest.getName();
        String givenAddress = patientRequest.getAddress();

        Patient patient = new Patient();
        patient.setAmka(givenId);
        patient.setName(givenName);
        patient.setAddress(givenAddress);
        patient.setUsername(AccountGenerator.generateUniqueUsername((ArrayList<String>) patientRepository.getAllUsernames()));
        patient.setPassword(AccountGenerator.generatePassword());

        return patientRepository.save(patient);
    }

    public void deletePatientById(String id) {
        Patient patient = this.getPatientById(id);
        patientRepository.delete(patient);
    }



}
