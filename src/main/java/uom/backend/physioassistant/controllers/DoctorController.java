package uom.backend.physioassistant.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.backend.physioassistant.auth.Authentication;
import uom.backend.physioassistant.dtos.requests.LoginRequest;
import uom.backend.physioassistant.dtos.responses.LoginResponse;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.services.DoctorService;

import java.util.List;

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
        try{
            Doctor doctor = this.doctorService.getById(id);

            return ResponseEntity.ok()
                    .body(doctor);
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    // Will be used for R1
    @PostMapping("/create")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        // Make sure the doctor is not already added
        try {
            this.doctorService.createDoctor(doctor);

            return ResponseEntity.ok()
                    .body(doctor);
        }
        catch (AlreadyAddedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .build();
        }
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

        try {
            Doctor foundDoctor = doctorService.getById(username);
            String correctPassword = foundDoctor.getPassword();

            // Validate Password
            if (password.equals(correctPassword))
                return ResponseEntity.ok(new LoginResponse(foundDoctor));
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("Wrong username or password."));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Wrong username or password."));
        }


    }
}
