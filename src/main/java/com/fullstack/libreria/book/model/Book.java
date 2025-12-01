package com.fullstack.libreria.book.model;

import java.time.Year;

import jakarta.persistence.*; // Librería JPA (maneja las entidades y mapeo a tablas)
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data; // Anotación de Lombok: genera getters, setters, toString, etc.

/**
 * Clase Libro
 * Representa la entidad principal del sistema.
 * Cada instancia de esta clase se traduce en una fila dentro de la tabla
 * "LIBRO" en la base de datos Oracle.
 */
@Data // Lombok genera automáticamente todos los getters y setters (ahorra código
      // repetitivo)
@Entity // Indica que esta clase es una entidad de JPA (se mapeará a una tabla)
@Table(name = "BOOK") // Nombre de la tabla en Oracle (opcional, si no se pone, toma el nombre de la
                       // clase)
public class Book {

    /**
     * Identificador único del libro.
     * 
     * @Id marca el atributo como clave primaria.
     * @GeneratedValue indica que el valor se genera automáticamente
     *                 (autoincremental).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título del libro.
     * Columna de tipo texto.
     */
    @NotBlank(message = "El título no puede ser nulo")
    @Size(min = 1, max = 50, message = "El título debe tener entre 1 y 50 caracteres")
    @Column(nullable = false, length = 100)
    private String title;

    /**
     * Autor del libro.
     */
    @NotBlank(message = "La autor es obligatorio")
    @Size(min = 1, max = 50, message = "El autor debe tener entre 1 y 50 caracteres")
    @Column(nullable = false, length = 100)
    private String author;

    /**
     * Género o categoría del libro.
     * Ejemplo: "Software", "Ficción", "Historia", etc.
     */
    @NotBlank(message = "El género es obligatorio")
    @Size(min = 1, max = 50, message = "El género debe tener entre 1 y 50 caracteres")
    @Column(nullable = false, length = 20)
    private String genre;

    /**
     * Año en que fue publicado el libro.
     * Ejemplo: 2008
     */
    @PastOrPresent(message = "El año de publicación no puede ser mayor que el año actual")
    @Column(nullable = false, length = 20)
    private Year publication;
}
