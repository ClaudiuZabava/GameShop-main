package com.awbd.gameshop.exceptions.exceptions;

import com.awbd.gameshop.exceptions.base.BaseException;
import org.springframework.http.HttpStatus;

public class NoFoundElementException extends BaseException {
    public NoFoundElementException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
