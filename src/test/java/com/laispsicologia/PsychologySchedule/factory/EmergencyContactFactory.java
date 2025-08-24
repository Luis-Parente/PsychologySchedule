package com.laispsicologia.PsychologySchedule.factory;

import com.laispsicologia.PsychologySchedule.emergencyContact.dto.EmergencyContactDTO;
import com.laispsicologia.PsychologySchedule.emergencyContact.entity.EmergencyContact;
import com.laispsicologia.PsychologySchedule.emergencyContact.entity.Relationship;

public class EmergencyContactFactory {

    public static EmergencyContact createEmergencyContact() {
        EmergencyContact contact = new EmergencyContact(1L, "Marcio Lemes", "marcio@gmail.com", "11973634545",
                Relationship.PARENT);
        contact.setClient(ClientFactory.createClient());
        return contact;
    }

    public static EmergencyContactDTO createEmergencyContactDto() {
        EmergencyContactDTO dto = new EmergencyContactDTO(createEmergencyContact());
        return dto;
    }
}
