package uom.backend.physioassistant.models.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "patient")
public class Patient extends User{
    @Id
    @Column(nullable = false)
    private String amka;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @ManyToMany(mappedBy = "patients")
    private List<Doctor> doctors = new ArrayList<>();

}
