package edu.miu.cs.cs489.courseproject.dental.web;

import edu.miu.cs.cs489.courseproject.dental.dto.patient.PatientRequest;
import edu.miu.cs.cs489.courseproject.dental.dto.patient.PatientResponse;
import edu.miu.cs.cs489.courseproject.dental.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OFFICE_MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<PatientResponse>> getPatients(@RequestParam(required = false) String search) {
        if (search == null || search.isBlank()) {
            return ResponseEntity.ok(patientService.getAllPatients());
        }
        return ResponseEntity.ok(patientService.searchPatients(search));
    }

    @GetMapping("/{patientId}")
    @PreAuthorize("hasAnyRole('OFFICE_MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getPatientById(patientId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OFFICE_MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRequest request) {
        return new ResponseEntity<>(patientService.createPatient(request), HttpStatus.CREATED);
    }

    @PutMapping("/{patientId}")
    @PreAuthorize("hasAnyRole('OFFICE_MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long patientId,
                                                         @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(patientId, request));
    }

    @DeleteMapping("/{patientId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.noContent().build();
    }
}
