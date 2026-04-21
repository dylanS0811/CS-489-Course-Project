package edu.miu.cs.cs489.courseproject.dental.web;

import edu.miu.cs.cs489.courseproject.dental.dto.dentist.DentistResponse;
import edu.miu.cs.cs489.courseproject.dental.dto.surgery.SurgeryResponse;
import edu.miu.cs.cs489.courseproject.dental.service.DirectoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DirectoryController {

    private final DirectoryService directoryService;

    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping("/dentists")
    @PreAuthorize("hasAnyRole('OFFICE_MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<DentistResponse>> getDentists() {
        return ResponseEntity.ok(directoryService.getDentists());
    }

    @GetMapping("/surgeries")
    @PreAuthorize("hasAnyRole('OFFICE_MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<SurgeryResponse>> getSurgeries() {
        return ResponseEntity.ok(directoryService.getSurgeries());
    }
}
