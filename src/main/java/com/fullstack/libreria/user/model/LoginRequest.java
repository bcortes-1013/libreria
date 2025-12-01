package com.fullstack.libreria.user.model;

import lombok.Data;

/**
 * ===============================================================
 * 📘 Clase: LoginRequest
 * ---------------------------------------------------------------
 * Semana 5 - DTO sencillo para la petición de inicio de sesión.
 *
 * Esta clase representa el cuerpo (JSON) que enviará el FrontEnd
 * al hacer login:
 *
 * {
 *   "email": "admin@biblioteca.cl",
 *   "password": "admin123"
 * }
 *
 * Mantenerlo separado de la entidad Usuario permite:
 *  - No depender de todos los campos de Usuario para autenticarse.
 *  - Explicar el uso de DTOs en el desarrollo FullStack.
 * ===============================================================
 */
@Data
public class LoginRequest {

    private String email;
    private String password;

}
