package edu.miu.cs.cs489.courseproject.dental.dto.appointment;

import edu.miu.cs.cs489.courseproject.dental.domain.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record AppointmentStatusRequest(
        @NotNull AppointmentStatus status
) {
}
