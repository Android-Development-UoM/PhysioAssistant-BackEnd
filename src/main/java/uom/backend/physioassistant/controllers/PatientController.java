package uom.backend.physioassistant.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.backend.physioassistant.auth.Authentication;
import uom.backend.physioassistant.dtos.requests.LoginRequest;
import uom.backend.physioassistant.dtos.responses.LoginResponse;
import uom.backend.physioassistant.exceptions.NotFoundException;
import uom.backend.physioassistant.models.users.Patient;
import uom.backend.physioassistant.services.PatientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController implements Authentication {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping()
    public ResponseEntity<List> getAllPatients() {
        List<Patient> patients = (List) patientService.getAllPatients();

        return ResponseEntity.ok()
                .body(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        Optional<Patient> foundPatient = patientService.getPatientById(id);

        if (foundPatient.isEmpty())
            throw new NotFoundException("Patient with AMKA: " + id + " not found.");

        return ResponseEntity.ok()
                .body(foundPatient.get());
    }

    @PostMapping("/create")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient addedPatient = patientService.createPatient(patient);

        return ResponseEntity.ok()
                .body(addedPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePatient(@PathVariable String id) {
        patientService.deletePatientById(id);
        return ResponseEntity.ok()
                .build();
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Optional<Patient> foundPatient = patientService.getPatientById(username);

        // Patient not found
        if (foundPatient.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Wrong username or password."));

        // Check if password is correct
        Patient patient = foundPatient.get();
        String correctPassword = patient.getPassword();

        if (password.equals(correctPassword))
            return ResponseEntity.ok(new LoginResponse(patient));
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Wrong username or password."));
    }
}
