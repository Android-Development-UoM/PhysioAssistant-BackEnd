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
@Table(name = "doctor")
public class Doctor extends User{
    @Id
    private String afm;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
}
