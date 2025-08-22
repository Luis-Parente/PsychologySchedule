package com.laispsicologia.PsychologySchedule.factory;

import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentMinDTO;
import com.laispsicologia.PsychologySchedule.appointment.entity.Appointment;
import com.laispsicologia.PsychologySchedule.appointment.entity.AppointmentStatus;

import java.time.LocalDateTime;

public class AppointmentFactory {

    public static Appointment createAppointment() {
        return new Appointment(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                AppointmentStatus.PENDING,
                100.0, true, ClientFactory.createClient());
    }

    public static AppointmentDTO createAppointmentDto() {
        Appointment appointment = createAppointment();
        return new AppointmentDTO(appointment);
    }

    public static AppointmentMinDTO createAppointmentMinDto() {
        Appointment appointment = createAppointment();
        return new AppointmentMinDTO(LocalDateTime.now(), true, ClientFactory.createClient().getId());
    }
}
