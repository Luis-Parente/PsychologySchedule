package com.laispsicologia.PsychologySchedule.factory;

import com.laispsicologia.PsychologySchedule.professional.dto.ProfessionalDTO;
import com.laispsicologia.PsychologySchedule.professional.entity.Professional;

public class ProfessionalFactory {

    public static Professional createProfessional() {
        return new Professional(1L, "Carla Maria", "CRP-RJ 123.123", "carla@gmail.com", "12 1234-1234");
    }

    public static ProfessionalDTO createProfessionalDTO() {
        return new ProfessionalDTO(createProfessional());
    }
}
