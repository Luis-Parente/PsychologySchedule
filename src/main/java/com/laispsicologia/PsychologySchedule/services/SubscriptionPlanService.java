package com.laispsicologia.PsychologySchedule.services;

import com.laispsicologia.PsychologySchedule.dto.SubscriptionPlanDTO;
import com.laispsicologia.PsychologySchedule.entities.SubscriptionPlan;
import com.laispsicologia.PsychologySchedule.repositories.AppointmentRepository;
import com.laispsicologia.PsychologySchedule.repositories.ClientRepository;
import com.laispsicologia.PsychologySchedule.repositories.SubscriptionPlanRepository;
import com.laispsicologia.PsychologySchedule.services.exceptions.AlreadyExistingPlanException;
import com.laispsicologia.PsychologySchedule.services.exceptions.InvalidDateException;
import com.laispsicologia.PsychologySchedule.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubscriptionPlanService {

    @Autowired
    private SubscriptionPlanRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Page<SubscriptionPlanDTO> findAll(Pageable pageable) {
        return repository.findAllActive(pageable).map(SubscriptionPlanDTO::new);
    }

    public SubscriptionPlanDTO findById(Long id) {
        SubscriptionPlan entity = getEntityById(id);
        return new SubscriptionPlanDTO(entity);
    }

    public SubscriptionPlanDTO insert(SubscriptionPlanDTO dto) {
        Boolean verifyClient = repository.getSubscriptionPlanByClientId(dto.getClientId());

        Boolean verifyPlanAvailability = repository.verifyPlanAvailability(dto.getStartDate());

        LocalDateTime endDate = dto.getStartDate().plusMinutes(dto.getAppointmentDurationInMinutes());
        Boolean verifyAppointmentAvailability = appointmentRepository.verifyAppointmentAvailability(dto.getStartDate(), endDate);

        if (verifyClient) throw new AlreadyExistingPlanException("This client already has a subscription plan");

        if (verifyPlanAvailability) throw new InvalidDateException("An subscription plan already exists at this date and time");

        if (verifyAppointmentAvailability) throw new InvalidDateException("An appointment already exists at this date and time");

        SubscriptionPlan entity = new SubscriptionPlan();
        copyDtoToEntity(dto, entity);
        repository.save(entity);
        return new SubscriptionPlanDTO(entity);
    }

    public SubscriptionPlanDTO update(Long id, SubscriptionPlanDTO dto) {
        SubscriptionPlan entity = getEntityById(id);
        copyDtoToEntity(dto, entity);
        repository.save(entity);

        return new SubscriptionPlanDTO(entity);
    }

    public void delete(Long id) {
        SubscriptionPlan entity = getEntityById(id);
        entity.softDelete();
        repository.save(entity);
    }

    private SubscriptionPlan getEntityById(Long id) {
        return repository.findByIdActive(id).orElseThrow(() -> new ResourceNotFoundException("SubscriptionPlan not found"));
    }

    private void copyDtoToEntity(SubscriptionPlanDTO dto, SubscriptionPlan entity) {
        entity.setAppointmentPrice(dto.getAppointmentPrice());
        entity.setAppointmentFrequency(dto.getAppointmentFrequency());
        entity.setStartDate(dto.getStartDate());
        entity.setAppointmentDurationInMinutes(dto.getAppointmentDurationInMinutes());
        entity.setClient(clientRepository.findByIdActive(dto.getClientId()).orElseThrow(() -> new ResourceNotFoundException("Client not found")));
    }
}
