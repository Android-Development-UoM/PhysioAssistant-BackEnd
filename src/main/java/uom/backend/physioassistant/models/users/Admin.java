package uom.backend.physioassistant.models.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter @Setter
@Table(name = "admin")
public class Admin extends User { }
