package edu.miu.cs.cs489.courseproject.dental.dto.bill;

import edu.miu.cs.cs489.courseproject.dental.domain.BillStatus;
import jakarta.validation.constraints.NotNull;

public record DentalBillStatusRequest(
        @NotNull BillStatus status
) {
}
