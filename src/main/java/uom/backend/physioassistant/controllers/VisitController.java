package uom.backend.physioassistant.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.backend.physioassistant.dtos.requests.CreateVisitRequest;
import uom.backend.physioassistant.models.Visit;
import uom.backend.physioassistant.services.VisitService;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
public class VisitController {
    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }


    @GetMapping
    public ResponseEntity<List<Visit>> getAllVisits() {
        List<Visit> visits = (List) this.visitService.getAllVisits();
        return ResponseEntity.ok()
                .body(visits);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<Visit>> getAllVisitsByPatientId(@PathVariable String id) {
        List<Visit> visits = (List) this.visitService.getVisitsByPatientId(id);
        return ResponseEntity.ok()
                .body(visits);
    }

    @PostMapping("/create")
    public Visit createVisit(@RequestBody CreateVisitRequest request) {
        return this.visitService.createVisit(request);
    }
}

