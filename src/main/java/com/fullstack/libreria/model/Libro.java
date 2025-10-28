package com.fullstack.libreria.model;

import jakarta.persistence.*; // Librería JPA (maneja las entidades y mapeo a tablas)
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
@Table(name = "LIBRO") // Nombre de la tabla en Oracle (opcional, si no se pone, toma el nombre de la
                       // clase)
public class Libro {

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
    private String titulo;

    /**
     * Autor del libro.
     */
    private String autor;

    /**
     * Año en que fue publicado el libro.
     * Ejemplo: 2008
     */
    private Integer anioPublicacion;

    /**
     * Género o categoría del libro.
     * Ejemplo: "Software", "Ficción", "Historia", etc.
     */
    private String genero;
}
