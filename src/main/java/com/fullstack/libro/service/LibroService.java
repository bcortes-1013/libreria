package com.fullstack.libro.service;

import org.springframework.stereotype.Service;

import com.fullstack.libro.model.Libro;
import com.fullstack.libro.repository.LibroRepository;

import java.util.List;
import java.util.Optional;

/**
 * Clase LibroService
 * ------------------
 * Capa intermedia entre el controlador (API REST) y el repositorio (Base de
 * Datos).
 * 
 * Aquí podemos aplicar reglas de negocio, validaciones o lógica adicional.
 * 
 * Por ejemplo: impedir guardar libros sin título o eliminar registros
 * inexistentes.
 */
@Service // Marca esta clase como un "servicio" dentro del contexto de Spring
public class LibroService {

    // Inyección automática del repositorio para acceder a la base de datos
    private final LibroRepository repository;

    // Constructor: Spring inyectará automáticamente una instancia de
    // LibroRepository
    public LibroService(LibroRepository repository) {
        this.repository = repository;
    }

    /**
     * Obtiene todos los libros de la base de datos.
     */
    public List<Libro> findAll() {
        return repository.findAll();
    }

    /**
     * Busca un libro por su ID.
     * Retorna un Optional (puede o no existir).
     */
    public Optional<Libro> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Guarda un nuevo libro o actualiza uno existente.
     * Si el ID es null → crea uno nuevo.
     * Si el ID existe → actualiza.
     */
    public Libro save(Libro libro) {
        return repository.save(libro);
    }

    /**
     * Elimina un libro por su ID.
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
