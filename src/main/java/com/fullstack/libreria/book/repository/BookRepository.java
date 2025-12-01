package com.fullstack.libreria.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fullstack.libreria.book.model.Book;

/**
 * Interfaz LibroRepository
 * ------------------------
 * Capa de acceso a datos (DAO).
 * 
 * Al extender JpaRepository, automáticamente tenemos disponibles todos los
 * métodos CRUD:
 * - findAll() → listar todos los registros
 * - findById() → buscar un registro por su ID
 * - save() → guardar o actualizar un registro
 * - deleteById() → eliminar un registro por su ID
 * 
 * NO es necesario implementar nada manualmente.
 * Spring Data JPA genera todo el código internamente.
 */
public interface BookRepository extends JpaRepository<Book, Long> {
    // Si en el futuro queremos buscar por "autor", por ejemplo:
    // List<Libro> findByAutor(String autor);
        // Ordenar por ID ascendente
    List<Book> findAllByOrderByIdAsc();
}