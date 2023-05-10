package uom.backend.physioassistant.models.users;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@MappedSuperclass
public abstract class User {
    @Column(nullable = false)
    private String password;
}
