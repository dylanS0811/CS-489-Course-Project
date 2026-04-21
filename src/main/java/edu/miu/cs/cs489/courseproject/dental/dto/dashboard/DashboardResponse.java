package edu.miu.cs.cs489.courseproject.dental.dto.dashboard;

import edu.miu.cs.cs489.courseproject.dental.dto.appointment.AppointmentResponse;

import java.util.List;

public record DashboardResponse(
        long patientCount,
        long dentistCount,
        long surgeryCount,
        long appointmentCount,
        long unpaidBillCount,
        List<AppointmentResponse> upcomingAppointments
) {
}
