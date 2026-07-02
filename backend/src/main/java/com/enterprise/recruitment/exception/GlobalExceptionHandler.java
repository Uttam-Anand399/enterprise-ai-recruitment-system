package com.enterprise.recruitment.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
	        ResourceNotFoundException exception
	) {

	    ApiErrorResponse error = new ApiErrorResponse(
	            Instant.now(),
	            HttpStatus.NOT_FOUND.value(),
	            "Not Found",
	            exception.getMessage()
	    );

	    return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationException(
	        MethodArgumentNotValidException exception
	) {

	    String message = exception.getBindingResult()
	            .getFieldErrors()
	            .stream()
	            .map(error -> error.getField() + " : " + error.getDefaultMessage())
	            .collect(Collectors.joining(", "));

	    ApiErrorResponse error = new ApiErrorResponse(
	            Instant.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "Validation Failed",
	            message
	    );

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(error);
	}

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(
            RuntimeException exception
    ) {

        ApiErrorResponse error = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

}