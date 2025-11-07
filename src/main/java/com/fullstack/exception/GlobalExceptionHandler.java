package com.fullstack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * ===============================================================
 * 📘 Clase: GlobalExceptionHandler
 * ---------------------------------------------------------------
 * Capa global para manejar todas las excepciones del sistema.
 *
 * 🔹 Semana 2:
 * - Captura y traduce excepciones a respuestas HTTP limpias y comprensibles.
 * - Centraliza el manejo de errores (400, 404 y 500).
 * - Integra logs para seguimiento profesional.
 * ===============================================================
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // ============================================================
    // 🔸 1. Manejo de errores de validación (400 Bad Request)
    // ============================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });

        log.warn("⚠️ Error de validación: {}", errores);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("errores", errores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // ============================================================
    // 🔸 2. Manejo de recursos no encontrados (404 Not Found)
    // ============================================================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex,
            WebRequest request) {
        log.error("❌ Recurso no encontrado: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("timestamp", LocalDateTime.now());
        error.put("error", ex.getMessage());
        error.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // ============================================================
    // 🔸 3. Manejo de errores generales (500 Internal Server Error)
    // ============================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex, WebRequest request) {
        log.error("💥 Error interno del servidor: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("timestamp", LocalDateTime.now());
        error.put("error", "Error interno del servidor: " + ex.getMessage());
        error.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
