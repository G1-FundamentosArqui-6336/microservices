package org.upc.deliveryservice.delivery.interfaces.rest.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.upc.deliveryservice.delivery.domain.exceptions.InvalidOrderStatusTransitionException;
import org.upc.deliveryservice.delivery.domain.exceptions.OrderNotFoundException;
import org.upc.deliveryservice.shared.interfaces.rest.resources.ErrorResponseResource;


import java.time.LocalDateTime;

@RestControllerAdvice // Anotación clave para que Spring lo reconozca como manejador global
public class DeliveryControllerAdvice {

    // Manejadores para entidades no encontradas (404 Not Found)
    @ExceptionHandler({
            OrderNotFoundException.class
    })
    public ResponseEntity<ErrorResponseResource> handleNotFoundException(RuntimeException ex, WebRequest request) {
        var errorResponse = new ErrorResponseResource(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Manejadores para conflictos de estado del negocio (409 Conflict)
    @ExceptionHandler({
            InvalidOrderStatusTransitionException.class
    })
    public ResponseEntity<ErrorResponseResource> handleBusinessStateConflictException(RuntimeException ex, WebRequest request) {
        var errorResponse = new ErrorResponseResource(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Manejador genérico para cualquier otra excepción no controlada (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseResource> handleGlobalException(Exception ex, WebRequest request) {
        var errorResponse = new ErrorResponseResource(
                LocalDateTime.now(),
                "Ocurrió un error inesperado en el contexto delivery",
                request.getDescription(false) + " | Details: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}