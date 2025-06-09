package com.testest.apisimulacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {


     // Pega os erros de validação (@Valid) e retorna uma resposta detalhada
     // usando a classe ErrorResponse.

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {

        // Cria uma mensagem de erro juntando os erros de campo
        String detailedMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("Campo '%s': %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                detailedMessage,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


     // Captura os erros que são lançados manualmente nos serviços
     // Usando sua classe ErrorResponse.

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                (HttpStatus) ex.getStatusCode(),
                // Pega a mensagem de erro
                ex.getReason(),
                request.getRequestURI());
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }
}