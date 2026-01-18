package com.github.bruce_mig.quick_bites.driver;

public class NoDriversAvailableException extends RuntimeException {
    public NoDriversAvailableException(String message) {
        super(message);
    }
}
