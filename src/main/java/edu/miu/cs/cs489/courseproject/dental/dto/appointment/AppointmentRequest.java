package edu.miu.cs.cs489.courseproject.dental.dto.appointment;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequest(
        @NotNull Long patientId,
        @NotNull Long dentistId,
        @NotNull Long surgeryId,
        @NotNull @FutureOrPresent LocalDate appointmentDate,
        @NotNull LocalTime appointmentTime,
        @Size(max = 512) String reason
) {
}
