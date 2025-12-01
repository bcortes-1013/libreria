package com.fullstack.libreria.user.model;

import jakarta.persistence.*; // JPA: mapeo objeto–relacional
import jakarta.validation.constraints.*; // Semana 2 → Bean Validation
import lombok.Data; // Lombok: getters/setters/toString
import java.time.LocalDate;

@Data
@Entity
@Table(name = "USERL", uniqueConstraints = {
        @UniqueConstraint(name = "UK_USERC_EMAIL", columnNames = "EMAIL")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 10, max = 100, message = "El nombre debe tener entre 10 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String fullName;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false, length = 200)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{6,}$", message = "La contraseña debe tener al menos una letra y un número")
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Column(nullable = false, length = 120, unique = true)
    private String email;

    @Pattern(regexp = "^$|^[0-9]{9,15}$", message = "El teléfono debe contener entre 9 y 15 dígitos")
    @Column(length = 20)
    private String phone;

    @PastOrPresent(message = "La fecha de registro no puede mayor a la actual")
    private LocalDate registerDate = LocalDate.now();

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "ADMIN|BIBLIOTECARIO|CLIENTE", message = "Rol inválido. Solo se permite ADMIN, BIBLIOTECARIO o USUARIO")
    @Column(nullable = false, length = 20)
    private String rol;
}
