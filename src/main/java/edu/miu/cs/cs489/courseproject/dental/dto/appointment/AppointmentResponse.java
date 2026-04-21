package edu.miu.cs.cs489.courseproject.dental.dto.appointment;

import edu.miu.cs.cs489.courseproject.dental.domain.AppointmentStatus;
import edu.miu.cs.cs489.courseproject.dental.dto.dentist.DentistResponse;
import edu.miu.cs.cs489.courseproject.dental.dto.patient.PatientSummaryResponse;
import edu.miu.cs.cs489.courseproject.dental.dto.surgery.SurgeryResponse;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponse(
        Long appointmentId,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        AppointmentStatus status,
        boolean confirmationSent,
        String reason,
        PatientSummaryResponse patient,
        DentistResponse dentist,
        SurgeryResponse surgery,
        String bookedBy
) {
}
