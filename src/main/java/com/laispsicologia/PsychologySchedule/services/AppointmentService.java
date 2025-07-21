package com.laispsicologia.PsychologySchedule.services;

import com.laispsicologia.PsychologySchedule.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.dto.AppointmentMinDTO;
import com.laispsicologia.PsychologySchedule.entities.Appointment;
import com.laispsicologia.PsychologySchedule.entities.enums.AppointmentStatus;
import com.laispsicologia.PsychologySchedule.repositories.AppointmentRepository;
import com.laispsicologia.PsychologySchedule.repositories.SubscriptionPlanRepository;
import com.laispsicologia.PsychologySchedule.services.exceptions.InvalidDateException;
import com.laispsicologia.PsychologySchedule.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository repository;

    @Autowired
    private SubscriptionPlanRepository planRepository;

    public List<AppointmentDTO> searchByDate(String initialDate, String finalDate) {
        try {
            if (initialDate.isBlank()) {
                initialDate = Instant.now().minus(15, ChronoUnit.DAYS).toString();
            }

            if (finalDate.isBlank()) {
                finalDate = Instant.now().plus(15, ChronoUnit.DAYS).toString();
            }

            Instant initialInstant = Instant.parse(initialDate);
            Instant finalInstant = Instant.parse(finalDate);

            initialDate = initialInstant.toString();
            finalDate = finalInstant.toString();

            return repository.searchByDate(initialDate, finalDate).stream()
                    .map(AppointmentDTO::new).toList();
        } catch (DateTimeParseException e) {
            throw new InvalidDateException("Invalid date format! Expected 'yyyy-MM-ddTHH:mm:ssZ'");
        }

    }

    public AppointmentDTO newAppointment(AppointmentMinDTO minDTO) {
        LocalDateTime endDate = LocalDateTime.parse(minDTO.getStartDate()).plus(minDTO.getAppointmentDuration());
        Boolean availability = repository.verifyAppointmentAvailability(minDTO.getStartDate(), endDate.toString());

        if (availability) throw new InvalidDateException("An appointment already exists at this date and time");

        Appointment newAppointment = createAppointment(minDTO);
        newAppointment = repository.save(newAppointment);

        return new AppointmentDTO(newAppointment);
    }

    private Appointment createAppointment(AppointmentMinDTO dto) {
        Appointment entity = new Appointment();
        entity.setAppointmentStatus(AppointmentStatus.PENDING);
        entity.setPrice(dto.getAppointmentPrice());
        entity.setStartTime(LocalDateTime.parse(dto.getStartDate()));
        entity.setEndTime(LocalDateTime.parse(dto.getStartDate()).plus(dto.getAppointmentDuration()));
        entity.setPaid(dto.getPaid());
        entity.setPlan(planRepository.findByIdActive(dto.getPlanId()).orElseThrow(() -> new ResourceNotFoundException("Subscription plan not found")));
        return entity;
    }
}
