package com.fullstack.libro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fullstack.libro.model.Libro;
import com.fullstack.libro.repository.LibroRepository;
import com.fullstack.libro.service.LibroService;

import java.util.List;

/**
 * Clase LibroController
 * ---------------------
 * Esta es la capa que expone los endpoints REST a los clientes (por ejemplo,
 * Postman o un frontend).
 * 
 * Es el “puente” entre las peticiones HTTP y la lógica de negocio.
 * 
 * Todas las rutas comienzan con /api/libros.
 */
@RestController // Indica que esta clase responderá solicitudes REST (formato JSON)
@RequestMapping("/api/libros") // Prefijo común para todas las rutas de este controlador
@CrossOrigin(origins = "*")
public class LibroController {

    private final LibroService service;
    private final LibroRepository repository;

    // Inyección de dependencias: el controlador recibe el servicio listo para usar
    public LibroController(LibroService service, LibroRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    /**
     * GET /api/libros
     * ----------------
     * Retorna la lista completa de libros almacenados.
     */
    @GetMapping
    public List<Libro> listar() {
        // return service.findAll();
        return repository.findAllOrderById();
    }

    /**
     * GET /api/libros/{id}
     * ---------------------
     * Retorna un solo libro por su ID.
     * Si no existe, devuelve un código 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok) // Si lo encuentra → 200 OK
                .orElse(ResponseEntity.notFound().build()); // Si no → 404
    }

    /**
     * POST /api/libros
     * -----------------
     * Crea un nuevo libro.
     * El objeto se envía como JSON en el cuerpo del request.
     */
    @PostMapping
    public ResponseEntity<Libro> crear(@RequestBody Libro libro) {
        Libro nuevo = service.save(libro);
        return ResponseEntity.status(201).body(nuevo); // Devuelve 201 Created
    }

    /**
     * PUT /api/libros/{id}
     * ---------------------
     * Actualiza un libro existente (mismo ID).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @RequestBody Libro libro) {
        return service.findById(id)
                .map(existente -> {
                    libro.setId(id); // Mantenemos el mismo ID
                    return ResponseEntity.ok(service.save(libro));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/libros/{id}
     * ------------------------
     * Elimina un libro por su ID.
     * Retorna 204 No Content si fue exitoso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build(); // No existe → 404
        }
        service.delete(id);
        return ResponseEntity.noContent().build(); // Eliminado → 204
    }
}
