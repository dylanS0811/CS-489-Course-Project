package edu.miu.cs.cs489.courseproject.dental.service;

import edu.miu.cs.cs489.courseproject.dental.domain.Address;
import edu.miu.cs.cs489.courseproject.dental.domain.Patient;
import edu.miu.cs.cs489.courseproject.dental.dto.address.AddressRequest;
import edu.miu.cs.cs489.courseproject.dental.dto.patient.PatientRequest;
import edu.miu.cs.cs489.courseproject.dental.dto.patient.PatientResponse;
import edu.miu.cs.cs489.courseproject.dental.exception.ResourceNotFoundException;
import edu.miu.cs.cs489.courseproject.dental.repository.AppointmentRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.DentalBillRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DentalBillRepository dentalBillRepository;
    private final MappingService mappingService;

    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
                          DentalBillRepository dentalBillRepository,
                          MappingService mappingService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.dentalBillRepository = dentalBillRepository;
        this.mappingService = mappingService;
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAllByOrderByLastNameAscFirstNameAsc()
                .stream()
                .map(mappingService::toPatientResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> searchPatients(String searchTerm) {
        String normalized = normalize(searchTerm);
        if (normalized == null || normalized.isBlank()) {
            return getAllPatients();
        }
        return patientRepository.search(normalized)
                .stream()
                .map(mappingService::toPatientResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long patientId) {
        return mappingService.toPatientResponse(findPatient(patientId));
    }

    public PatientResponse createPatient(PatientRequest request) {
        Patient patient = new Patient(
                normalize(request.patientNumber()),
                normalize(request.firstName()),
                normalize(request.lastName()),
                normalize(request.phoneNumber()),
                normalize(request.email()),
                request.dateOfBirth(),
                toAddress(request.mailingAddress())
        );
        return mappingService.toPatientResponse(patientRepository.save(patient));
    }

    public PatientResponse updatePatient(Long patientId, PatientRequest request) {
        Patient patient = findPatient(patientId);
        patient.setPatientNumber(normalize(request.patientNumber()));
        patient.setFirstName(normalize(request.firstName()));
        patient.setLastName(normalize(request.lastName()));
        patient.setPhoneNumber(normalize(request.phoneNumber()));
        patient.setEmail(normalize(request.email()));
        patient.setDateOfBirth(request.dateOfBirth());
        updateAddress(patient.getMailingAddress(), request.mailingAddress());
        return mappingService.toPatientResponse(patientRepository.save(patient));
    }

    public void deletePatient(Long patientId) {
        Patient patient = findPatient(patientId);
        dentalBillRepository.deleteAllByPatient(patient);
        appointmentRepository.deleteAllByPatient(patient);
        patientRepository.delete(patient);
    }

    public Patient findPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient with id %d was not found.".formatted(patientId)));
    }

    private Address toAddress(AddressRequest request) {
        return new Address(
                normalize(request.street()),
                normalize(request.city()),
                normalize(request.state()),
                normalize(request.zipCode())
        );
    }

    private void updateAddress(Address address, AddressRequest request) {
        address.setStreet(normalize(request.street()));
        address.setCity(normalize(request.city()));
        address.setState(normalize(request.state()));
        address.setZipCode(normalize(request.zipCode()));
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
