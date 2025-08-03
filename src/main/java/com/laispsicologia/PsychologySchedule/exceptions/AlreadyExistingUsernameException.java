package com.laispsicologia.PsychologySchedule.exceptions;

import java.io.Serial;

public class AlreadyExistingUsernameException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AlreadyExistingUsernameException(String msg) {
        super(msg);
    }
}
