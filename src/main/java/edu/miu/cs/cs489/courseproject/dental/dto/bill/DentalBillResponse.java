package edu.miu.cs.cs489.courseproject.dental.dto.bill;

import edu.miu.cs.cs489.courseproject.dental.domain.BillStatus;
import edu.miu.cs.cs489.courseproject.dental.dto.patient.PatientSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DentalBillResponse(
        Long billId,
        PatientSummaryResponse patient,
        Long appointmentId,
        String appointmentLabel,
        LocalDate issueDate,
        BigDecimal amount,
        BillStatus status,
        String description
) {
}
