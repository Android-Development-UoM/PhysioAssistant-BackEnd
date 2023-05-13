package uom.backend.physioassistant.models.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "patient")
public class Patient extends User{
    @Column(nullable = false)
    private String amka;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "username")
    private Doctor doctor;
}
