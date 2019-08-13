package com.rabobank.customer.statementsprocessor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * All unknown exceptions are handled here.
 * The actual exceptions are logged for further action
 * User is responded with a graceful error message
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler
{
    Logger logger = LoggerFactory.getLogger("CustomExceptionHandler");
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        // Logging the actual error for administratie purpose
        details.forEach(d -> logger.error(d));

        // Returning a graceful error message to the end user
        return new ResponseEntity("Invalid Request.  Please check all inputs and retry or contact admin.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}