package com.course.change.demo.exception;

import org.springframework.stereotype.Component;

@Component
public class TelegramBotNotAvailableToAddNewUserException extends RuntimeException {
    public TelegramBotNotAvailableToAddNewUserException() {
        super(""); //TODO
    }
}
