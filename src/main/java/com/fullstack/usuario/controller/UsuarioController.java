package com.fullstack.usuario.controller;

import com.fullstack.usuario.model.Usuario;
import com.fullstack.usuario.service.UsuarioService;
import jakarta.validation.Valid; // Semana 2 → activa Bean Validation
import lombok.extern.slf4j.Slf4j; // Logs profesionales
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ===============================================================
 * 📘 Clase: UsuarioController
 * ---------------------------------------------------------------
 * Expone la API REST del microservicio Usuario.
 *
 * 🔹 Semana 2:
 * - Endpoints CRUD con @Valid y ResponseEntity.
 * - Códigos HTTP correctos (200/201/204/400/404).
 * - Endpoints de consultas personalizadas (email, rol).
 * ===============================================================
 */
@Slf4j
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // ============================================================
    // 🔸 CRUD
    // ============================================================

    /**
     * GET /api/usuarios
     * Lista todos los usuarios.
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        log.info("👥 [GET] Listar usuarios");
        return ResponseEntity.ok(service.listar());
    }

    /**
     * GET /api/usuarios/{id}
     * Obtiene un usuario por ID (404 si no existe).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
        log.info("🔍 [GET] Obtener usuario ID: {}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /**
     * POST /api/usuarios
     * Crea un usuario nuevo (valida datos).
     */
    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody Usuario usuario) {
        log.info("📝 [POST] Crear usuario: {}", usuario.getEmail());
        Usuario creado = service.crear(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * PUT /api/usuarios/{id}
     * Actualiza un usuario existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id,
            @Valid @RequestBody Usuario usuario) {
        log.info("✏️ [PUT] Actualizar usuario ID: {}", id);
        return ResponseEntity.ok(service.actualizar(id, usuario));
    }

    /**
     * DELETE /api/usuarios/{id}
     * Elimina un usuario (204 si todo OK).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("🗑️ [DELETE] Eliminar usuario ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // 🔸 Consultas personalizadas (complejidad)
    // ============================================================

    /**
     * GET /api/usuarios/email/{email}
     * Busca un usuario por email (404 si no existe).
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable String email) {
        log.info("📧 [GET] Buscar por email: {}", email);
        return ResponseEntity.ok(service.buscarPorEmail(email));
    }

    /**
     * GET /api/usuarios/rol/{rol}
     * Lista usuarios por rol (ADMIN o ANALISTA).
     */
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> listarPorRol(@PathVariable String rol) {
        log.info("🎯 [GET] Usuarios por rol: {}", rol);
        List<Usuario> usuarios = service.listarPorRol(rol);
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(usuarios);
    }
}
