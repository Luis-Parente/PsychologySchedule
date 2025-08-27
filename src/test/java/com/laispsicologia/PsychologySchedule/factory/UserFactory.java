package com.laispsicologia.PsychologySchedule.factory;

import com.laispsicologia.PsychologySchedule.user.dto.UserRegisterDTO;
import com.laispsicologia.PsychologySchedule.user.dto.UserReturnDTO;
import com.laispsicologia.PsychologySchedule.user.entity.Role;
import com.laispsicologia.PsychologySchedule.user.entity.User;

public class UserFactory {

    public static User createUser() {
        return new User(1L, "Jair Claudio", "jair@gmail.com", "123456789", Role.ADMIN);
    }

    public static UserRegisterDTO createUserRegisterDto() {
        return new UserRegisterDTO("Jair Claudio", "jair@gmail.com\"", "123456789", Role.ADMIN);
    }

    public static UserReturnDTO createUserReturnDto() {
        return new UserReturnDTO(createUser());
    }
}
