package org.rippleosi.config;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.exception.InvalidDataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 */
@ControllerAdvice
@ResponseBody
public class DefaultControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(DataNotFoundException.class)
    public String handleDataNotFoundException(DataNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataException.class)
    public String handleInvalidDataException(InvalidDataException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ConfigurationException.class)
    public String handleConfigurationException(ConfigurationException ex) {
        return ex.getMessage();
    }
}
