package com.laispsicologia.PsychologySchedule.appointment;

import com.laispsicologia.PsychologySchedule.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM tb_appointment WHERE start_time BETWEEN :firstDate AND :lastDate AND deleted_at IS NULL")
    Page<Appointment> findFilteredByDate(LocalDateTime firstDate, LocalDateTime lastDate, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT EXISTS (SELECT 1 FROM tb_appointment WHERE ((:startTime BETWEEN start_time AND end_time) " +
            "OR (:endTime BETWEEN start_time AND end_time AND :endTime != start_time))" +
            "AND appointment_status = 3 " +
            "AND deleted_at IS NULL)")
    Boolean verifyAppointmentAvailability(LocalDateTime startTime, LocalDateTime endTime);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_appointment WHERE id = :id AND deleted_at IS NULL")
    Optional<Appointment> findByIdActive(Long id);
}
