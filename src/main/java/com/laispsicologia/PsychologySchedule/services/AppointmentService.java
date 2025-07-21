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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository repository;

    @Autowired
    private SubscriptionPlanRepository planRepository;

    public Page<AppointmentDTO> searchByDate(LocalDateTime initialDate, LocalDateTime finalDate, Pageable pageable) {
        if (initialDate == null) {
            initialDate = LocalDateTime.now().minusDays(15);
        }

        if (finalDate == null) {
            finalDate = LocalDateTime.now().plusDays(15);
        }

        return repository.searchByDate(initialDate, finalDate, pageable).map(AppointmentDTO::new);
    }

    public AppointmentDTO newAppointment(AppointmentMinDTO minDTO) {
        LocalDateTime endDate = minDTO.getStartDate().plus(minDTO.getAppointmentDuration());
        Boolean availability = repository.verifyAppointmentAvailability(minDTO.getStartDate(), endDate);

        if (availability) throw new InvalidDateException("An appointment already exists at this date and time");

        Appointment newAppointment = createAppointment(minDTO);
        newAppointment = repository.save(newAppointment);

        return new AppointmentDTO(newAppointment);
    }

    private Appointment createAppointment(AppointmentMinDTO dto) {
        Appointment entity = new Appointment();
        entity.setAppointmentStatus(AppointmentStatus.PENDING);
        entity.setPrice(dto.getAppointmentPrice());
        entity.setStartTime(dto.getStartDate());
        entity.setEndTime(dto.getStartDate().plus(dto.getAppointmentDuration()));
        entity.setPaid(dto.getPaid());
        entity.setPlan(planRepository.findByIdActive(dto.getPlanId()).orElseThrow(() -> new ResourceNotFoundException("Subscription plan not found")));
        return entity;
    }
}
