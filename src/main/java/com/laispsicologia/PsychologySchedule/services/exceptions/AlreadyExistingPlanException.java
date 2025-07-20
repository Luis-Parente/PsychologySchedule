package com.laispsicologia.PsychologySchedule.services.exceptions;

import java.io.Serial;

public class AlreadyExistingPlanException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AlreadyExistingPlanException(String msg) {
        super(msg);
    }

}
