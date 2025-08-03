package com.laispsicologia.PsychologySchedule.security.token.dto;

public class LoginResponseDTO {

    private String token;

    public LoginResponseDTO() {

    }

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
