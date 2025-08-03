package com.laispsicologia.PsychologySchedule.exceptions;

import java.io.Serial;

public class TokenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TokenException(String msg) {
        super(msg);
    }
}
