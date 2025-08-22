package com.laispsicologia.PsychologySchedule.appointment;

import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentMinDTO;
import com.laispsicologia.PsychologySchedule.appointment.entity.Appointment;
import com.laispsicologia.PsychologySchedule.client.ClientRepository;
import com.laispsicologia.PsychologySchedule.client.entity.Client;
import com.laispsicologia.PsychologySchedule.exceptions.InvalidDateException;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import com.laispsicologia.PsychologySchedule.factory.AppointmentFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class AppointmentServiceTests {

    @InjectMocks
    private AppointmentService service;

    @Mock
    private AppointmentRepository repository;

    @Mock
    private ClientRepository clientRepository;

    private Long validId, invalidId;

    private Long validClientId, invalidClientId;

    private LocalDateTime validDate, invalidDate;

    private PageImpl<Appointment> page;

    Pageable pageable;

    private Appointment appointment;

    private AppointmentDTO dto;

    private AppointmentMinDTO minDto;

    private Client client;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 2000L;

        validClientId = 1L;

        invalidId = 1000L;

        validDate = LocalDateTime.now();
        invalidDate = LocalDateTime.parse("2025-08-01T13:00:00");

        appointment = AppointmentFactory.createAppointment();

        page = new PageImpl<>(List.of(appointment));

        pageable = PageRequest.of(0, 10);

        dto = AppointmentFactory.createAppointmentDto();

        minDto = AppointmentFactory.createAppointmentMinDto();

        client = appointment.getClient();

        Mockito.when(
                        repository.findFilteredByDate(any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(page);

        Mockito.when(repository.findByIdActive(validId)).thenReturn(Optional.of(appointment));
        Mockito.when(repository.findByIdActive(invalidId)).thenReturn(Optional.empty());

        Mockito.when(clientRepository.findByIdActive(validClientId)).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.findByIdActive(invalidClientId)).thenReturn(Optional.empty());

        Mockito.when(repository.verifyAppointmentAvailability(validDate, validDate.plusHours(1))).thenReturn(false);
        Mockito.when(repository.verifyAppointmentAvailability(invalidDate, invalidDate.plusHours(1))).thenReturn(true);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(appointment);
    }

    @Test
    public void findFilteredByDateShouldReturnPageOfAppointmentDTOWhenDatesAreNotNull() {
        Page<AppointmentDTO> result = service.findFilteredByDate(validDate.minusDays(15).toLocalDate(),
                validDate.plusDays(15).toLocalDate(), pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(1, result.getSize());
        Assertions.assertEquals(appointment.getId(), result.getContent().get(0).getId());
        Assertions.assertEquals(appointment.getAppointmentStatus().toString(),
                result.getContent().get(0).getAppointmentStatus());
        Assertions.assertEquals(appointment.getPrice(), result.getContent().get(0).getPrice());
        Assertions.assertEquals(appointment.getPaid(), result.getContent().get(0).getPaid());
        Assertions.assertEquals(appointment.getClient().getId(), result.getContent().get(0).getClientId());
    }

    @Test
    public void findFilteredByDateShouldReturnPageOfAppointmentDTOWhenDatesAreNull() {
        Page<AppointmentDTO> result = service.findFilteredByDate(null, null, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(1, result.getSize());
        Assertions.assertEquals(appointment.getId(), result.getContent().get(0).getId());
        Assertions.assertEquals(appointment.getAppointmentStatus().toString(),
                result.getContent().get(0).getAppointmentStatus());
        Assertions.assertEquals(appointment.getPrice(), result.getContent().get(0).getPrice());
        Assertions.assertEquals(appointment.getPaid(), result.getContent().get(0).getPaid());
        Assertions.assertEquals(appointment.getClient().getId(), result.getContent().get(0).getClientId());
    }

    @Test
    public void findByIdShouldReturnAppointmentDTOWhenValidIdAndDeletedAtIsNull() {
        AppointmentDTO result = service.findById(validId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(appointment.getId(), result.getId());
        Assertions.assertEquals(appointment.getAppointmentStatus().toString(),
                result.getAppointmentStatus());
        Assertions.assertEquals(appointment.getPrice(), result.getPrice());
        Assertions.assertEquals(appointment.getPaid(), result.getPaid());
        Assertions.assertEquals(appointment.getClient().getId(), result.getClientId());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenInvalidId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(invalidId);
        });
    }

    @Test
    public void insertShouldReturnAppointmentDTOWhenValidDate() {
        AppointmentDTO result = service.insert(minDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(appointment.getId(), result.getId());
        Assertions.assertEquals(appointment.getAppointmentStatus().toString(),
                result.getAppointmentStatus());
        Assertions.assertEquals(appointment.getPrice(), result.getPrice());
        Assertions.assertEquals(appointment.getPaid(), result.getPaid());
        Assertions.assertEquals(appointment.getClient().getId(), result.getClientId());
    }

    @Test
    public void insertShouldThrowInvalidDateExceptionWhenInvalidDate() {
        minDto.setStartTime(invalidDate);
        Assertions.assertThrows(InvalidDateException.class, () -> {
            service.insert(minDto);
        });
    }

    @Test
    public void insertShouldThrowResourceNotFoundExceptionWhenInvalidClientId() {
        minDto.setClientId(invalidClientId);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.insert(minDto);
        });
    }

    @Test
    public void updateShouldReturnAppointmentDtoWhenValidAppointmentId() {
        AppointmentDTO result = service.update(validId, dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(validId, result.getId());
        Assertions.assertEquals(appointment.getAppointmentStatus().toString(),
                result.getAppointmentStatus());
        Assertions.assertEquals(appointment.getPrice(), result.getPrice());
        Assertions.assertEquals(appointment.getPaid(), result.getPaid());
        Assertions.assertEquals(appointment.getClient().getId(), result.getClientId());
    }

    @Test
    public void updateShouldThrowResourceNotFoundWhenInvalidAppointmentId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(invalidId, dto);
        });
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenValidAppointmentIdAndInvalidClientId() {
        dto.setClientId(invalidClientId);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(validId, dto);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(validId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(invalidId);
        });
    }
}
