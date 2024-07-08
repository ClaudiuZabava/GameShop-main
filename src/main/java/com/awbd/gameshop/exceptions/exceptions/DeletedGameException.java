package com.awbd.gameshop.exceptions.exceptions;

import com.awbd.gameshop.exceptions.base.BaseException;
import org.springframework.http.HttpStatus;

public class DeletedGameException extends BaseException {
    public DeletedGameException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
