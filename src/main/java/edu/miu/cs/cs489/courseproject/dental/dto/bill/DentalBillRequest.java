package edu.miu.cs.cs489.courseproject.dental.dto.bill;

import edu.miu.cs.cs489.courseproject.dental.domain.BillStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DentalBillRequest(
        @NotNull Long patientId,
        Long appointmentId,
        @NotNull LocalDate issueDate,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotNull BillStatus status,
        @NotBlank @Size(max = 255) String description
) {
}
