package uom.backend.physioassistant.models.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Id
    private String amka;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;

}
