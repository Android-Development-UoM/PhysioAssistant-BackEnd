package uom.backend.physioassistant.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.backend.physioassistant.auth.Authentication;
import uom.backend.physioassistant.dtos.requests.LoginRequest;
import uom.backend.physioassistant.dtos.responses.LoginResponse;
import uom.backend.physioassistant.exceptions.NotFoundException;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.services.DoctorService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController implements Authentication {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping()
    public ResponseEntity<List> getAllDoctors() {
        List<Doctor> doctors = (List) doctorService.getAllDoctors();

        return ResponseEntity.ok()
                .body(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable String id) {
        Optional<Doctor> foundDoctor = doctorService.getById(id);

        if (foundDoctor.isEmpty())
            throw new NotFoundException("Doctor with id: " + id + " not found.");

        Doctor doctor = foundDoctor.get();
        return ResponseEntity.ok()
                .body(doctor);
    }

    // Will be used for R1
    @PostMapping("/create")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        // Make sure the doctor is not already added
        String givenId = doctor.getAfm();
        Optional doctor_flag = this.doctorService.getById(givenId);

        if (doctor_flag.isPresent())
            return ResponseEntity.status(406)
                    .build();

        this.doctorService.createDoctor(doctor);

        return ResponseEntity.ok()
                .body(doctor);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteById(@PathVariable String id) {
        doctorService.deleteById(id);

        return ResponseEntity.ok()
                .build();
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Optional<Doctor> foundDoctor = doctorService.getById(username);

        // Doctor not found
        if (foundDoctor.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Wrong username or password."));

        // Check if password is correct
        Doctor doctor = foundDoctor.get();
        String correctPassword = doctor.getPassword();

        if (password.equals(correctPassword))
            return ResponseEntity.ok(new LoginResponse(doctor));
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Wrong username or password."));
    }
}
