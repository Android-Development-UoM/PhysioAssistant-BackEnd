package uom.backend.physioassistant.dtos.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uom.backend.physioassistant.models.appointment.Appointment;
import uom.backend.physioassistant.models.appointment.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AppointmentDTO {
    private Long id;
    private String doctorId;
    private String doctorName;
    private String patientId;
    private String patientName;
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus status;

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();

        this.doctorId = appointment.getDoctor().getAfm();
        this.doctorName = appointment.getDoctor().getName();

        this.patientId = appointment.getPatient().getAmka();
        this.patientName = appointment.getPatient().getName();

        this.date = appointment.getDate();
        this.time = appointment.getTime();
        this.status = appointment.getStatus();
    }
}
