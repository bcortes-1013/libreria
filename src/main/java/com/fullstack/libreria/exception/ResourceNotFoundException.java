package com.fullstack.libreria.exception;

/**
 * ===============================================================
 * 📘 Clase: ResourceNotFoundException
 * ---------------------------------------------------------------
 * Excepción personalizada para manejar errores de tipo "404 - No encontrado".
 *
 * 🔹 Semana 2:
 * - Esta clase se usa cuando un recurso solicitado (Libro, Usuario, etc.)
 * no existe en la base de datos.
 * - Se lanzará desde los servicios (por ejemplo, LibroService)
 * y será manejada globalmente en GlobalExceptionHandler.
 * ===============================================================
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param message Descripción del error (ej: "Libro no encontrado con ID: 5").
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
