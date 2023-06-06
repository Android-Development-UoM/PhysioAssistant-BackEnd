package uom.backend.physioassistant.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.models.PhysioAction;
import uom.backend.physioassistant.services.PhysioActionService;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class PhysioActionController {
    private final PhysioActionService physioActionService;

    public PhysioActionController(PhysioActionService physioActionService) {
        this.physioActionService = physioActionService;
    }

    @GetMapping()
    public ResponseEntity<List> getAllActions() {
        List<PhysioAction> actions = (List) physioActionService.getAllActions();

        return ResponseEntity.ok()
                .body(actions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhysioAction> getActionById(@PathVariable String id) {
        PhysioAction action = physioActionService.getById(id);

        return ResponseEntity.ok()
                .body(action);
    }

    // Will be used for R2
    @PostMapping("/create")
    public ResponseEntity<PhysioAction> createPhysioAction(@RequestBody PhysioAction action) {
        try {
            this.physioActionService.createPhysioAction(action);

            return ResponseEntity.ok()
                    .body(action);
        }
        catch (AlreadyAddedException e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null);
        }
        catch (RuntimeException e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteActionById(@PathVariable String id) {
        this.physioActionService.deleteById(id);

        return ResponseEntity.ok()
                .build();
    }
}
