package com.laispsicologia.PsychologySchedule.appointment;

import com.laispsicologia.PsychologySchedule.appointment.entity.Appointment;
import com.laispsicologia.PsychologySchedule.appointment.entity.AppointmentStatus;
import com.laispsicologia.PsychologySchedule.factory.AppointmentFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
public class AppointmentRepositoryTests {

    @Autowired
    private AppointmentRepository repository;

    private Long validId, invalidId, countTotalClients;

    private LocalDateTime validDate, invalidDate;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 2000L;
        countTotalClients = 2L;

        validDate = LocalDateTime.now();
        invalidDate = LocalDateTime.parse("2025-08-01T13:00:00");
    }

    @Test
    public void saveShouldPersistClientWithAutoIncrementWhenIdIsNull() {
        Appointment appointment = AppointmentFactory.createAppointment();
        appointment.setId(null);

        Appointment result = repository.save(appointment);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(countTotalClients + 1, result.getId());
        Assertions.assertEquals(AppointmentStatus.PENDING, result.getAppointmentStatus());
        Assertions.assertEquals(100.0, result.getPrice());
        Assertions.assertEquals(true, result.getPaid());
        Assertions.assertEquals(1L, result.getClient().getId());
    }

    @Test
    public void saveShouldUpdateClientDataWhenIdExists() {
        Appointment appointment = AppointmentFactory.createAppointment();
        appointment.setId(validId);

        Appointment result = repository.save(appointment);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(validId, result.getId());
        Assertions.assertEquals(AppointmentStatus.PENDING, result.getAppointmentStatus());
        Assertions.assertEquals(100.0, result.getPrice());
        Assertions.assertEquals(true, result.getPaid());
        Assertions.assertEquals(1L, result.getClient().getId());
    }

    @Test
    public void findFilteredByDateShouldReturnPageOfClient() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Appointment> result = repository.findFilteredByDate(LocalDateTime.now(), LocalDateTime.now().plusDays(30),
                pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
    }

    @Test
    public void verifyAppointmentAvailabilityShouldReturnTrueWhenInvalidDate() {
        Boolean result = repository.verifyAppointmentAvailability(invalidDate, invalidDate.plusHours(1));

        Assertions.assertEquals(true, result);
    }

    @Test
    public void verifyAppointmentAvailabilityShouldReturnFalseWhenValidDate() {
        Boolean result = repository.verifyAppointmentAvailability(validDate, validDate.plusHours(1));

        Assertions.assertEquals(false, result);
    }

    @Test
    public void findByIdActiveShouldReturnNotEmptyOptionalWhenValidIdAndDeletedAtIsNull() {
        Optional<Appointment> result = repository.findByIdActive(validId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(validId, result.get().getId());
        Assertions.assertEquals(LocalDateTime.parse("2025-08-01T13:00:00"), result.get().getStartTime());
        Assertions.assertEquals(LocalDateTime.parse("2025-08-01T14:00:00"), result.get().getEndTime());
        Assertions.assertEquals(AppointmentStatus.PENDING, result.get().getAppointmentStatus());
        Assertions.assertEquals(80.0, result.get().getPrice());
        Assertions.assertEquals(false, result.get().getPaid());
        Assertions.assertEquals(1L, result.get().getClient().getId());
    }

    @Test
    public void findByIdActiveShouldReturnEmptyOptionalWhenInvalidId() {
        Optional<Appointment> result = repository.findByIdActive(invalidId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByIdActiveShouldReturnEmptyOptionalWhenValidIdAndDeletedAtIsNotNull() {
        Optional<Appointment> appointment = repository.findByIdActive(validId);
        appointment.get().softDelete();
        repository.save(appointment.get());

        Optional<Appointment> result = repository.findByIdActive(validId);

        Assertions.assertTrue(result.isEmpty());
    }
}
