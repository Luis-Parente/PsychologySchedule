package com.laispsicologia.PsychologySchedule.user.dto;

import com.laispsicologia.PsychologySchedule.user.entity.Role;
import com.laispsicologia.PsychologySchedule.user.entity.User;

public class UserReturnDTO {

    private Long id;
    private String name;
    private String email;
    private Role role;

    public UserReturnDTO() {

    }

    public UserReturnDTO(Long id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public UserReturnDTO(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.role = entity.getRole();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
