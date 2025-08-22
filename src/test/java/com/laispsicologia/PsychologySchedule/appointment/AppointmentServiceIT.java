package com.laispsicologia.PsychologySchedule.appointment;

import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentMinDTO;
import com.laispsicologia.PsychologySchedule.appointment.entity.AppointmentStatus;
import com.laispsicologia.PsychologySchedule.client.ClientRepository;
import com.laispsicologia.PsychologySchedule.exceptions.InvalidDateException;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import com.laispsicologia.PsychologySchedule.factory.AppointmentFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class AppointmentServiceIT {

    @Autowired
    private AppointmentService service;

    @Autowired
    private AppointmentRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    private Long validId, invalidId;

    private Long invalidClientId;

    private Long countTotalAppointments;

    private LocalDateTime validDate, invalidDate;

    private Pageable pageable;

    private AppointmentDTO dto;

    private AppointmentMinDTO minDto;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 1000L;

        countTotalAppointments = 2L;

        invalidClientId = 1000L;

        validDate = LocalDateTime.now();
        invalidDate = LocalDateTime.parse("2025-08-01T13:00:00");

        pageable = PageRequest.of(0, 10);

        dto = AppointmentFactory.createAppointmentDto();

        minDto = AppointmentFactory.createAppointmentMinDto();
    }

    @Test
    public void findFilteredByDateShouldReturnPageOfAppointmentDTOWhenDatesAreNotNull() {
        Page<AppointmentDTO> result = service.findFilteredByDate(validDate.minusDays(15).toLocalDate(),
                validDate.plusDays(15).toLocalDate(), pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
    }

    @Test
    public void findFilteredByDateShouldReturnPageOfAppointmentDTOWhenDatesAreNull() {
        Page<AppointmentDTO> result = service.findFilteredByDate(null, null, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
    }

    @Test
    public void findByIdShouldReturnAppointmentDTOWhenValidIdAndDeletedAtIsNull() {
        AppointmentDTO result = service.findById(validId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(validId, result.getId());
        Assertions.assertEquals(AppointmentStatus.PENDING.toString(),
                result.getAppointmentStatus());
        Assertions.assertEquals(80.0, result.getPrice());
        Assertions.assertEquals(false, result.getPaid());
        Assertions.assertEquals(1, result.getClientId());
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
        Assertions.assertEquals(countTotalAppointments + 1, result.getId());
        Assertions.assertEquals(AppointmentStatus.PENDING.toString(),
                result.getAppointmentStatus());
        Assertions.assertEquals(80.0, result.getPrice());
        Assertions.assertEquals(minDto.getPaid(), result.getPaid());
        Assertions.assertEquals(minDto.getClientId(), result.getClientId());
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
        Assertions.assertEquals(dto.getAppointmentStatus(),
                result.getAppointmentStatus());
        Assertions.assertEquals(dto.getPrice(), result.getPrice());
        Assertions.assertEquals(dto.getPaid(), result.getPaid());
        Assertions.assertEquals(dto.getClientId(), result.getClientId());
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
