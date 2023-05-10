package uom.backend.physioassistant.auth;

import org.springframework.http.ResponseEntity;
import uom.backend.physioassistant.dtos.requests.LoginRequest;
import uom.backend.physioassistant.dtos.responses.LoginResponse;

public interface Authentication {
    ResponseEntity<LoginResponse> login(LoginRequest loginRequest);

}
