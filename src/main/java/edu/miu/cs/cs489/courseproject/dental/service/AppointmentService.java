package edu.miu.cs.cs489.courseproject.dental.service;

import edu.miu.cs.cs489.courseproject.dental.domain.AppUser;
import edu.miu.cs.cs489.courseproject.dental.domain.Appointment;
import edu.miu.cs.cs489.courseproject.dental.domain.AppointmentStatus;
import edu.miu.cs.cs489.courseproject.dental.domain.BillStatus;
import edu.miu.cs.cs489.courseproject.dental.domain.Dentist;
import edu.miu.cs.cs489.courseproject.dental.domain.Patient;
import edu.miu.cs.cs489.courseproject.dental.domain.Surgery;
import edu.miu.cs.cs489.courseproject.dental.dto.appointment.AppointmentRequest;
import edu.miu.cs.cs489.courseproject.dental.dto.appointment.AppointmentResponse;
import edu.miu.cs.cs489.courseproject.dental.exception.BusinessRuleViolationException;
import edu.miu.cs.cs489.courseproject.dental.exception.ResourceNotFoundException;
import edu.miu.cs.cs489.courseproject.dental.repository.AppUserRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.AppointmentRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.DentalBillRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.DentistRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.PatientRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.SurgeryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;

@Service
@Transactional
public class AppointmentService {

    private static final int MAX_ACTIVE_APPOINTMENTS_PER_DENTIST_WEEK = 5;

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final SurgeryRepository surgeryRepository;
    private final AppUserRepository appUserRepository;
    private final DentalBillRepository dentalBillRepository;
    private final MappingService mappingService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DentistRepository dentistRepository,
                              SurgeryRepository surgeryRepository,
                              AppUserRepository appUserRepository,
                              DentalBillRepository dentalBillRepository,
                              MappingService mappingService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.dentistRepository = dentistRepository;
        this.surgeryRepository = surgeryRepository;
        this.appUserRepository = appUserRepository;
        this.dentalBillRepository = dentalBillRepository;
        this.mappingService = mappingService;
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAllByOrderByAppointmentDateAscAppointmentTimeAsc()
                .stream()
                .map(mappingService::toAppointmentResponse)
                .toList();
    }

    public AppointmentResponse scheduleAppointment(AppointmentRequest request, String bookedByUsername) {
        Patient patient = findPatient(request.patientId());
        Dentist dentist = findDentist(request.dentistId());
        Surgery surgery = findSurgery(request.surgeryId());
        AppUser bookedBy = appUserRepository.findByUsername(bookedByUsername)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User '%s' was not found.".formatted(bookedByUsername)));

        ensurePatientCanBook(patient);
        ensureDentistCanAccept(dentist, request);

        Appointment appointment = new Appointment(
                request.appointmentDate(),
                request.appointmentTime(),
                AppointmentStatus.SCHEDULED,
                true,
                normalize(request.reason()),
                patient,
                dentist,
                surgery,
                bookedBy
        );

        try {
            return mappingService.toAppointmentResponse(appointmentRepository.save(appointment));
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessRuleViolationException(
                    "The selected dentist already has an appointment at that date and time.");
        }
    }

    public AppointmentResponse updateStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = findAppointment(appointmentId);
        appointment.setStatus(status);
        return mappingService.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    public AppointmentResponse cancelAppointment(Long appointmentId) {
        return updateStatus(appointmentId, AppointmentStatus.CANCELLED);
    }

    private void ensurePatientCanBook(Patient patient) {
        boolean hasUnpaidBill = dentalBillRepository.existsByPatient_PatientIdAndStatus(
                patient.getPatientId(), BillStatus.UNPAID);
        if (hasUnpaidBill) {
            throw new BusinessRuleViolationException(
                    "Patient %s has an outstanding unpaid dental-service bill.".formatted(patient.getPatientNumber()));
        }
    }

    private void ensureDentistCanAccept(Dentist dentist, AppointmentRequest request) {
        if (appointmentRepository.existsByDentist_DentistIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
                dentist.getDentistId(),
                request.appointmentDate(),
                request.appointmentTime(),
                AppointmentStatus.CANCELLED
        )) {
            throw new BusinessRuleViolationException(
                    "The selected dentist already has an appointment at that date and time.");
        }

        LocalDate weekStart = request.appointmentDate().with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);
        long weeklyCount = appointmentRepository.countByDentist_DentistIdAndAppointmentDateBetweenAndStatusIn(
                dentist.getDentistId(),
                weekStart,
                weekEnd,
                EnumSet.of(AppointmentStatus.SCHEDULED, AppointmentStatus.COMPLETED)
        );
        if (weeklyCount >= MAX_ACTIVE_APPOINTMENTS_PER_DENTIST_WEEK) {
            throw new BusinessRuleViolationException(
                    "A dentist cannot be assigned more than five active appointments in a given week.");
        }
    }

    private Appointment findAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment with id %d was not found.".formatted(appointmentId)));
    }

    private Patient findPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient with id %d was not found.".formatted(patientId)));
    }

    private Dentist findDentist(Long dentistId) {
        return dentistRepository.findById(dentistId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Dentist with id %d was not found.".formatted(dentistId)));
    }

    private Surgery findSurgery(Long surgeryId) {
        return surgeryRepository.findById(surgeryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Surgery with id %d was not found.".formatted(surgeryId)));
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
