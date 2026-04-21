package edu.miu.cs.cs489.courseproject.dental.service;

import edu.miu.cs.cs489.courseproject.dental.domain.Appointment;
import edu.miu.cs.cs489.courseproject.dental.domain.BillStatus;
import edu.miu.cs.cs489.courseproject.dental.domain.DentalBill;
import edu.miu.cs.cs489.courseproject.dental.domain.Patient;
import edu.miu.cs.cs489.courseproject.dental.dto.bill.DentalBillRequest;
import edu.miu.cs.cs489.courseproject.dental.dto.bill.DentalBillResponse;
import edu.miu.cs.cs489.courseproject.dental.exception.BusinessRuleViolationException;
import edu.miu.cs.cs489.courseproject.dental.exception.ResourceNotFoundException;
import edu.miu.cs.cs489.courseproject.dental.repository.AppointmentRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.DentalBillRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.PatientRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BillingService {

    private final DentalBillRepository dentalBillRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final MappingService mappingService;

    public BillingService(DentalBillRepository dentalBillRepository,
                          PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
                          MappingService mappingService) {
        this.dentalBillRepository = dentalBillRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.mappingService = mappingService;
    }

    @Transactional(readOnly = true)
    public List<DentalBillResponse> getBills(BillStatus status) {
        List<DentalBill> bills = status == null
                ? dentalBillRepository.findAllByOrderByIssueDateDescBillIdDesc()
                : dentalBillRepository.findByStatusOrderByIssueDateDescBillIdDesc(status);
        return bills.stream()
                .map(mappingService::toDentalBillResponse)
                .toList();
    }

    public DentalBillResponse createBill(DentalBillRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient with id %d was not found.".formatted(request.patientId())));
        Appointment appointment = null;
        if (request.appointmentId() != null) {
            appointment = appointmentRepository.findById(request.appointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Appointment with id %d was not found.".formatted(request.appointmentId())));
            if (!appointment.getPatient().getPatientId().equals(patient.getPatientId())) {
                throw new BusinessRuleViolationException(
                        "The selected appointment does not belong to the selected patient.");
            }
        }

        DentalBill bill = new DentalBill(
                patient,
                appointment,
                request.issueDate(),
                request.amount(),
                request.status(),
                normalize(request.description())
        );

        try {
            return mappingService.toDentalBillResponse(dentalBillRepository.save(bill));
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessRuleViolationException(
                    "The selected appointment already has a dental-service bill.");
        }
    }

    public DentalBillResponse updateStatus(Long billId, BillStatus status) {
        DentalBill bill = dentalBillRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Dental bill with id %d was not found.".formatted(billId)));
        bill.setStatus(status);
        return mappingService.toDentalBillResponse(dentalBillRepository.save(bill));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
