package edu.miu.cs.cs489.courseproject.dental.web;

import edu.miu.cs.cs489.courseproject.dental.domain.BillStatus;
import edu.miu.cs.cs489.courseproject.dental.dto.bill.DentalBillRequest;
import edu.miu.cs.cs489.courseproject.dental.dto.bill.DentalBillResponse;
import edu.miu.cs.cs489.courseproject.dental.dto.bill.DentalBillStatusRequest;
import edu.miu.cs.cs489.courseproject.dental.service.BillingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bills")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OFFICE_MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<DentalBillResponse>> getBills(@RequestParam(required = false) BillStatus status) {
        return ResponseEntity.ok(billingService.getBills(status));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OFFICE_MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<DentalBillResponse> createBill(@Valid @RequestBody DentalBillRequest request) {
        return new ResponseEntity<>(billingService.createBill(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{billId}/status")
    @PreAuthorize("hasAnyRole('OFFICE_MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<DentalBillResponse> updateStatus(@PathVariable Long billId,
                                                           @Valid @RequestBody DentalBillStatusRequest request) {
        return ResponseEntity.ok(billingService.updateStatus(billId, request.status()));
    }
}
