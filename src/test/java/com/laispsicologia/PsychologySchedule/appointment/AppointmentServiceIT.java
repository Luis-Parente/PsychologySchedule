package com.laispsicologia.PsychologySchedule.appointment;

import com.laispsicologia.PsychologySchedule.client.ClientRepository;
import com.laispsicologia.PsychologySchedule.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AppointmentServiceIT {

    @Autowired
    private ClientService service;

    @Autowired
    private AppointmentRepository repository;

    @Autowired
    private ClientRepository clientRepository;
}
