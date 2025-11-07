package com.fullstack.usuario.model;

import jakarta.persistence.*; // JPA: mapeo objeto–relacional
import jakarta.validation.constraints.*; // Semana 2 → Bean Validation
import lombok.Data; // Lombok: getters/setters/toString
import java.time.LocalDate;

/**
 * ===============================================================
 * 📘 Clase: Usuario
 * ---------------------------------------------------------------
 * Representa un usuario del sistema (tabla USUARIO en Oracle).
 *
 * 🔹 Semana 1:
 * (No existía este dominio)
 * 🔹 Semana 2:
 * - Se crea el segundo microservicio “Usuario” para cumplir la pauta.
 * - Se agregan validaciones de datos (Bean Validation).
 * - Se documenta cada campo para uso docente.
 * ===============================================================
 */
@Data
@Entity
@Table(name = "USUARIO", uniqueConstraints = {
        // 🔸 Opcional (Semana 2): garantía de unicidad de email en BD
        @UniqueConstraint(name = "UK_USUARIO_EMAIL", columnNames = "EMAIL")
})
public class Usuario {

    // ============================================================
    // 🔸 Identificador primario (Semana 2)
    // ============================================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ============================================================
    // 🔸 Datos personales con validaciones (Semana 2)
    // ============================================================

    /**
     * Nombre completo del usuario.
     * Reglas: obligatorio, 3–100 caracteres.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Correo electrónico de contacto (único).
     * Reglas: formato email, obligatorio, único.
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Column(nullable = false, length = 120, unique = true)
    private String email;

    /**
     * Teléfono de contacto (opcional).
     * Patrón simple: solo dígitos, 9–15 caracteres.
     * (Adecuar al país si se desea)
     */
    @Pattern(regexp = "^$|^[0-9]{9,15}$", message = "El teléfono debe contener entre 9 y 15 dígitos")
    @Column(length = 20)
    private String telefono;

    /**
     * Fecha de registro en el sistema.
     * Reglas: presente o pasado. Por defecto: hoy.
     */
    @PastOrPresent(message = "La fecha de registro no puede ser futura")
    @Column(name = "FECHA_REGISTRO")
    private LocalDate fechaRegistro = LocalDate.now();

    /**
     * Rol funcional simple (sin seguridad aún).
     * Reglas: ADMIN o ANALISTA (texto).
     * (Esto prepara el terreno para Seguridad en semanas posteriores)
     */
    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "ADMIN|ANALISTA", message = "Rol inválido. Solo se permite ADMIN o ANALISTA")
    @Column(nullable = false, length = 20)
    private String rol;

    // ============================================================
    // 🧠 Notas Semana 2:
    // - Las validaciones se activan en el Controller con @Valid.
    // - Si fallan, se captura MethodArgumentNotValidException en el
    // GlobalExceptionHandler.
    // ============================================================
}
