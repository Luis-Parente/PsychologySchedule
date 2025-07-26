package com.laispsicologia.PsychologySchedule.exceptions;

import java.io.Serial;

public class InvalidDateException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidDateException(String msg) {
        super(msg);
    }
}
