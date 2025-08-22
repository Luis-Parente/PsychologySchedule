package com.laispsicologia.PsychologySchedule.factory;

import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentMinDTO;
import com.laispsicologia.PsychologySchedule.appointment.entity.Appointment;
import com.laispsicologia.PsychologySchedule.appointment.entity.AppointmentStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AppointmentFactory {

    public static Appointment createAppointment() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        return new Appointment(1L, now.plusHours(1), now.plusHours(2), AppointmentStatus.PENDING, 100.0, true,
                ClientFactory.createClient());
    }

    public static AppointmentDTO createAppointmentDto() {
        Appointment appointment = createAppointment();
        return new AppointmentDTO(appointment);
    }

    public static AppointmentMinDTO createAppointmentMinDto() {
        Appointment appointment = createAppointment();
        return new AppointmentMinDTO(appointment.getStartTime(), true, ClientFactory.createClient().getId());
    }
}
