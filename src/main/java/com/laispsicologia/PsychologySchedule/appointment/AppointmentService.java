package com.laispsicologia.PsychologySchedule.appointment;

import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentMinDTO;
import com.laispsicologia.PsychologySchedule.appointment.entity.Appointment;
import com.laispsicologia.PsychologySchedule.appointment.entity.AppointmentStatus;
import com.laispsicologia.PsychologySchedule.client.ClientRepository;
import com.laispsicologia.PsychologySchedule.client.entity.Client;
import com.laispsicologia.PsychologySchedule.exceptions.InvalidDateException;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    public Page<AppointmentDTO> findFilteredByDate(LocalDateTime firstDate, LocalDateTime lastDate, Pageable pageable) {
        if (firstDate == null) {
            firstDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        }
        if (lastDate == null) {
            lastDate = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59)
                    .withSecond(59);
        }
        Page<Appointment> result = repository.findFilteredByDate(firstDate, lastDate, pageable);
        return result.map(AppointmentDTO::new);
    }

    public AppointmentDTO findById(Long id) {
        Appointment entity = getEntityById(id);
        return new AppointmentDTO(entity);
    }

    public AppointmentDTO insert(AppointmentMinDTO dto) {
        Appointment newAppointment = createNewAppoint(dto);
        if (repository.verifyAppointmentAvailability(newAppointment.getStartTime(), newAppointment.getEndTime()))
            throw new InvalidDateException("Already exists an appointment in this date and time");
        return new AppointmentDTO(repository.save(newAppointment));
    }

    public AppointmentDTO update(Long id, @Valid AppointmentDTO dto) {
        Appointment entity = getEntityById(id);
        copyDtoToEntity(dto, entity);
        return new AppointmentDTO(repository.save(entity));
    }

    public void delete(Long id) {
        Appointment entity = getEntityById(id);
        entity.softDelete();
        repository.save(entity);
    }

    private Appointment createNewAppoint(AppointmentMinDTO dto) {
        Appointment newAppointment = new Appointment();
        Client client = clientRepository.findByIdActive(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        newAppointment.setStartTime(dto.getStartTime());
        newAppointment.setEndTime(dto.getStartTime().plusMinutes(client.getAppointmentDurationInMinutes()));
        newAppointment.setAppointmentStatus(AppointmentStatus.valueOf("PENDING"));
        newAppointment.setPrice(client.getAppointmentPrice());
        newAppointment.setPaid(dto.getPaid());
        newAppointment.setClient(client);
        return newAppointment;
    }

    private void copyDtoToEntity(AppointmentDTO dto, Appointment entity) {
        Client client = clientRepository.findByIdActive(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setAppointmentStatus(AppointmentStatus.valueOf(dto.getAppointmentStatus()));
        entity.setPrice(dto.getPrice());
        entity.setPaid(dto.getPaid());
        entity.setClient(client);
    }

    private Appointment getEntityById(Long id) {
        return repository.findByIdActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }
}
