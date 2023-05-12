package uom.backend.physioassistant.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.backend.physioassistant.models.appointment.Appointment;
import uom.backend.physioassistant.models.appointment.AppointmentStatus;
import uom.backend.physioassistant.services.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping()
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = (List) this.appointmentService.getAllAppointments();

        return ResponseEntity.ok()
                .body(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        try {
            Appointment appointment = this.appointmentService.getAppointmentById(id);

            return ResponseEntity.ok()
                    .body(appointment);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctorId(@RequestParam(name = "did") String doctorId) {
        System.out.println(doctorId);
        List<Appointment> appointments = (List) this.appointmentService.getAppointmentsBasedOnDoctorId(doctorId);

        return ResponseEntity.ok()
                .body(appointments);
    }

    @GetMapping("/patient")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientId(@RequestParam(name = "pid") String patientId) {
        List<Appointment> appointments = (List) this.appointmentService.getAppointmentsBasedOnPatientId(patientId);

        return ResponseEntity.ok()
                .body(appointments);
    }

    @GetMapping("/pending/patient")
    public ResponseEntity<List<Appointment>> getPendingByPatientId(@RequestParam(name = "pid") String patientId) {
        List<Appointment> appointments = (List) this.appointmentService.getPendingByPatientId(patientId);

        return ResponseEntity.ok()
                .body(appointments);
    }

    @GetMapping("/pending/doctor")
    public ResponseEntity<List<Appointment>> getPendingByDoctorId(@RequestParam(name = "did") String doctorId) {
        List<Appointment> appointments = (List) this.appointmentService.getPendingByDoctorId(doctorId);

        return ResponseEntity.ok()
                .body(appointments);
    }

    @GetMapping("/accepted/doctor")
    public ResponseEntity<List<Appointment>> getAcceptedByDoctorId(@RequestParam(name = "did") String doctorId) {
        List<Appointment> appointments = (List) this.appointmentService.getAcceptedByDoctorId(doctorId);

        return ResponseEntity.ok()
                .body(appointments);
    }

    @GetMapping("/accepted/patient")
    public ResponseEntity<List<Appointment>> getAcceptedByPatientId(@RequestParam(name = "pid") String patientId) {
        List<Appointment> appointments = (List) this.appointmentService.getAcceptedByPatientId(patientId);

        return ResponseEntity.ok()
                .body(appointments);
    }

    @PutMapping("/update/status")
    public ResponseEntity updateAppointmentStatus(
            @RequestParam(name = "aid") Long appointmentId,
            @RequestParam(name = "status") AppointmentStatus status) {

        try {
            this.appointmentService.setAppointmentStatus(appointmentId, status);
            return ResponseEntity.ok()
                    .build();
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        Appointment createdAppointment = this.appointmentService.createAppointment(appointment);

        return ResponseEntity.ok()
                .body(createdAppointment);
    }
}
