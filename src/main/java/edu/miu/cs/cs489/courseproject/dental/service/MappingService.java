package edu.miu.cs.cs489.courseproject.dental.service;

import edu.miu.cs.cs489.courseproject.dental.domain.Address;
import edu.miu.cs.cs489.courseproject.dental.domain.Appointment;
import edu.miu.cs.cs489.courseproject.dental.domain.Dentist;
import edu.miu.cs.cs489.courseproject.dental.domain.Patient;
import edu.miu.cs.cs489.courseproject.dental.domain.Surgery;
import edu.miu.cs.cs489.courseproject.dental.dto.address.AddressResponse;
import edu.miu.cs.cs489.courseproject.dental.dto.appointment.AppointmentResponse;
import edu.miu.cs.cs489.courseproject.dental.dto.dentist.DentistResponse;
import edu.miu.cs.cs489.courseproject.dental.dto.patient.PatientResponse;
import edu.miu.cs.cs489.courseproject.dental.dto.patient.PatientSummaryResponse;
import edu.miu.cs.cs489.courseproject.dental.dto.surgery.SurgeryResponse;
import org.springframework.stereotype.Component;

@Component
public class MappingService {

    public AddressResponse toAddressResponse(Address address) {
        return new AddressResponse(
                address.getAddressId(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZipCode()
        );
    }

    public PatientResponse toPatientResponse(Patient patient) {
        return new PatientResponse(
                patient.getPatientId(),
                patient.getPatientNumber(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getFullName(),
                patient.getPhoneNumber(),
                patient.getEmail(),
                patient.getDateOfBirth(),
                toAddressResponse(patient.getMailingAddress())
        );
    }

    public PatientSummaryResponse toPatientSummary(Patient patient) {
        return new PatientSummaryResponse(
                patient.getPatientId(),
                patient.getPatientNumber(),
                patient.getFullName(),
                patient.getPhoneNumber(),
                patient.getEmail()
        );
    }

    public DentistResponse toDentistResponse(Dentist dentist) {
        return new DentistResponse(
                dentist.getDentistId(),
                dentist.getDentistCode(),
                dentist.getFirstName(),
                dentist.getLastName(),
                dentist.getFullName(),
                dentist.getPhoneNumber(),
                dentist.getEmail(),
                dentist.getSpecialization()
        );
    }

    public SurgeryResponse toSurgeryResponse(Surgery surgery) {
        return new SurgeryResponse(
                surgery.getSurgeryId(),
                surgery.getSurgeryNumber(),
                surgery.getName(),
                surgery.getPhoneNumber(),
                toAddressResponse(surgery.getLocationAddress())
        );
    }

    public AppointmentResponse toAppointmentResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getAppointmentId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus(),
                appointment.isConfirmationSent(),
                appointment.getReason(),
                toPatientSummary(appointment.getPatient()),
                toDentistResponse(appointment.getDentist()),
                toSurgeryResponse(appointment.getSurgery()),
                appointment.getBookedBy().getFullName()
        );
    }
}
