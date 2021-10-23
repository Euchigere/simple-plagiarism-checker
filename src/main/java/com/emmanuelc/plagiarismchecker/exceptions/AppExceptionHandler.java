package com.emmanuelc.plagiarismchecker.exceptions;

import com.emmanuelc.plagiarismchecker.domain.dto.ApiResponse;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AppCustomException.class)
    public ResponseEntity<Object> handleAppCustomException (AppCustomException exe) {
        return buildResponseEntity(exe.getMessage(), exe.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException (ResourceNotFoundException exe) {
        return buildResponseEntity(exe.getMessage(), exe.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception) {
        return buildResponseEntity("Bad credentials", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return buildResponseEntity(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException(IOException exe) {
        return buildResponseEntity(exe.getLocalizedMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UncheckedIOException.class)
    public ResponseEntity<Object> handleIOException(UncheckedIOException exe) {
        return buildResponseEntity(exe.getLocalizedMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException cve) {
        return buildResponseEntity(
                getValidationErrors(cve.getConstraintViolations()),
                "constraint violations",
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(
                getValidationErrors(ex.getBindingResult().getFieldErrors()),
                "Violation Errors",
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(
                getValidationErrors(ex.getBindingResult().getFieldErrors()),
                "please fill in all required fields",
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL());
        return buildResponseEntity(message, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity("Error writing JSON output", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> buildResponseEntity(String message, HttpStatus status) {
        final ApiResponse<?> response = new ApiResponse<>();
        response.setMessage(message);
        response.setData(null);
        response.setStatus(false);
        return buildResponseEntity(response, status);
    }

    private ResponseEntity<Object> buildResponseEntity(Map<String, String> errors, String message, HttpStatus status) {
        final ApiResponse<Map<String, String>> response = new ApiResponse<>();
        response.setMessage(message);
        response.setData(errors);
        response.setStatus(false);
        return buildResponseEntity(response, status);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiResponse<?> apiResponse, HttpStatus status) {
        return new ResponseEntity<>(apiResponse, status);
    }

    private Map<String, String> getValidationErrors(Set<ConstraintViolation<?>> violations) {
        Map<String, String> errors = new HashMap<>();
        violations.forEach(e -> errors.put(((PathImpl) e.getPropertyPath()).getLeafNode().asString(), e.getMessage()));
        return errors;
    }

    private Map<String, String> getValidationErrors(List<FieldError> fieldErrors) {
        Map<String, String> errors = new HashMap<>();
        fieldErrors.forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        return errors;
    }
}
