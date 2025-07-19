package com.laispsicologia.PsychologySchedule.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.laispsicologia.PsychologySchedule.entities.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	@Query(nativeQuery = true, value = "SELECT * FROM tb_appointment WHERE start_time BETWEEN :initialDate AND :finalDate AND deleted_at IS NULL")
	List<Appointment> searchByDate(String initialDate, String finalDate);

}
