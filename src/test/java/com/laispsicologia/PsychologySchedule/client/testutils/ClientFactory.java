package com.laispsicologia.PsychologySchedule.client.testutils;

import com.laispsicologia.PsychologySchedule.client.dto.ClientDTO;
import com.laispsicologia.PsychologySchedule.client.dto.ClientMinDTO;
import com.laispsicologia.PsychologySchedule.client.entity.Client;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClientFactory {

    public static Client createClient() {
        Client client = new Client(1L, "Nelson Jose", "123.456.789-00", LocalDate.parse("2002-07-02"),
                "nelson@gmail.com", "91234-1234",
                90.0, 14, LocalDateTime.parse("2025-08-25T13:00:00"), null, 60L);
        return client;
    }

    public static ClientDTO createClientDto() {
        Client client = createClient();
        return new ClientDTO(client);
    }

    public static ClientMinDTO createClientMinDto() {
        Client client = createClient();
        return new ClientMinDTO(client);
    }
}
