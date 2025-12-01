package com.fullstack.libreria.book.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fullstack.libreria.book.model.Book;
import com.fullstack.libreria.book.repository.BookRepository;
import com.fullstack.libreria.book.service.BookService;

import java.util.List;

/**
 * Clase LibroController
 * ---------------------
 * Esta es la capa que expone los endpoints REST a los clientes (por ejemplo,
 * Postman o un frontend).
 * 
 * Es el “puente” entre las peticiones HTTP y la lógica de negocio.
 * 
 * Todas las rutas comienzan con /api/books.
 */
@RestController // Indica que esta clase responderá solicitudes REST (formato JSON)
@RequestMapping("/api/books") // Prefijo común para todas las rutas de este controlador
@CrossOrigin(origins = "*")
public class BookController {

    private final BookService service;
    private final BookRepository repository;

    // Inyección de dependencias: el controlador recibe el servicio listo para usar
    public BookController(BookService service, BookRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    /**
     * GET /api/books
     * ----------------
     * Retorna la lista completa de libros almacenados.
     */
    @GetMapping
    public List<Book> listar() {
        // return service.findAll();
        return repository.findAllByOrderByIdAsc();
    }

    /**
     * GET /api/books/{id}
     * ---------------------
     * Retorna un solo libro por su ID.
     * Si no existe, devuelve un código 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok) // Si lo encuentra → 200 OK
                .orElse(ResponseEntity.notFound().build()); // Si no → 404
    }

    /**
     * POST /api/books
     * -----------------
     * Crea un nuevo libro.
     * El objeto se envía como JSON en el cuerpo del request.
     */
    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book libro) {
        Book nuevo = service.save(libro);
        return ResponseEntity.status(201).body(nuevo); // Devuelve 201 Created
    }

    /**
     * PUT /api/books/{id}
     * ---------------------
     * Actualiza un libro existente (mismo ID).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book libro) {
        return service.findById(id)
                .map(existente -> {
                    libro.setId(id); // Mantenemos el mismo ID
                    return ResponseEntity.ok(service.save(libro));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/books/{id}
     * ------------------------
     * Elimina un libro por su ID.
     * Retorna 204 No Content si fue exitoso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build(); // No existe → 404
        }
        service.delete(id);
        return ResponseEntity.noContent().build(); // Eliminado → 204
    }
}
