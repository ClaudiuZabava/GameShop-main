package com.awbd.gameshop.exceptions.exceptions;

import com.awbd.gameshop.exceptions.base.BaseException;
import org.springframework.http.HttpStatus;

public class UserNotLoggedInException extends BaseException {
    public UserNotLoggedInException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
