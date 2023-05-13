package uom.backend.physioassistant.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.backend.physioassistant.dtos.models.AppointmentDTO;
import uom.backend.physioassistant.dtos.requests.CreateAppointmentRequest;
import uom.backend.physioassistant.models.appointment.Appointment;
import uom.backend.physioassistant.models.appointment.AppointmentStatus;
import uom.backend.physioassistant.services.AppointmentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping()
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<Appointment> appointments = (List) this.appointmentService.getAllAppointments();

        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(appointment -> new AppointmentDTO(appointment))
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(appointmentDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        try {
            Appointment appointment = this.appointmentService.getAppointmentById(id);

            return ResponseEntity.ok()
                    .body(new AppointmentDTO(appointment));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctorId(@RequestParam(name = "did") String doctorId) {
        System.out.println(doctorId);
        List<Appointment> appointments = (List) this.appointmentService.getAppointmentsBasedOnDoctorId(doctorId);

        List<AppointmentDTO> appointmentDTOs = appointments
                .stream().map(appointment -> new AppointmentDTO(appointment))
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(appointmentDTOs);
    }

    @GetMapping("/patient")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatientId(@RequestParam(name = "pid") String patientId) {
        List<Appointment> appointments = (List) this.appointmentService.getAppointmentsBasedOnPatientId(patientId);

        List<AppointmentDTO> appointmentDTOs = appointments
                .stream().map(appointment -> new AppointmentDTO(appointment))
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(appointmentDTOs);
    }

    @GetMapping("/doctor/{doctorId}/status/{status}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsForDoctorByStatus(
            @PathVariable String doctorId, @PathVariable AppointmentStatus status
    ){
        List<Appointment> appointments = (List) this.appointmentService.getAllForDoctorByStatus(doctorId, status);
        List<AppointmentDTO> appointmentDTOS = appointments
                .stream().map(appointment -> new AppointmentDTO(appointment))
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(appointmentDTOS);
    }

    @GetMapping("/patient/{patientId}/status/{status}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsForPatientByStatus(
            @PathVariable String patientId, @PathVariable AppointmentStatus status
    ){
        List<Appointment> appointments = (List) this.appointmentService.getAllForPatientByStatus(patientId, status);
        List<AppointmentDTO> appointmentDTOS = appointments
                .stream().map(appointment -> new AppointmentDTO(appointment))
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(appointmentDTOS);
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
    public ResponseEntity<Appointment> createAppointment(@RequestBody CreateAppointmentRequest appointmentRequest) {
        Appointment createdAppointment = this.appointmentService.createAppointment(appointmentRequest);

        return ResponseEntity.ok()
                .body(createdAppointment);
    }
}
