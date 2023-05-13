package uom.backend.physioassistant.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CreatePatientRequest {
    private String amka;
    private String name;
    private String address;
    private String username;
    private String password;
    private String doctorId;
}
