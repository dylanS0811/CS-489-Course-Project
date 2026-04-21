package edu.miu.cs.cs489.courseproject.dental.web;

import edu.miu.cs.cs489.courseproject.dental.dto.appointment.AppointmentRequest;
import edu.miu.cs.cs489.courseproject.dental.dto.appointment.AppointmentResponse;
import edu.miu.cs.cs489.courseproject.dental.dto.appointment.AppointmentStatusRequest;
import edu.miu.cs.cs489.courseproject.dental.security.ClinicUserDetails;
import edu.miu.cs.cs489.courseproject.dental.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OFFICE_MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<AppointmentResponse>> getAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OFFICE_MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<AppointmentResponse> scheduleAppointment(@Valid @RequestBody AppointmentRequest request,
                                                                   @AuthenticationPrincipal ClinicUserDetails user) {
        return new ResponseEntity<>(
                appointmentService.scheduleAppointment(request, user.getUsername()),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{appointmentId}/status")
    @PreAuthorize("hasAnyRole('OFFICE_MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<AppointmentResponse> updateStatus(@PathVariable Long appointmentId,
                                                            @Valid @RequestBody AppointmentStatusRequest request) {
        return ResponseEntity.ok(appointmentService.updateStatus(appointmentId, request.status()));
    }

    @DeleteMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('OFFICE_MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentId));
    }
}
