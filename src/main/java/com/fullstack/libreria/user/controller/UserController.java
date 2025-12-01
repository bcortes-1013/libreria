package com.fullstack.libreria.user.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.fullstack.libreria.exception.ResourceNotFoundException;
import com.fullstack.libreria.user.model.LoginRequest;
import com.fullstack.libreria.user.model.User;
import com.fullstack.libreria.user.repository.UserRepository;
import com.fullstack.libreria.user.service.UserService;

import java.util.List;
import java.util.UUID;

/**
 * ===============================================================
 * 📘 Clase: UsuarioController
 * ---------------------------------------------------------------
 * Expone la API REST del microservicio user.
 *
 * 🔹 Semana 2:
 * - Endpoints CRUD con @Valid y ResponseEntity.
 * - Códigos HTTP correctos (200/201/204/400/404).
 * - Endpoints de consultas personalizadas (email, rol).
 * ===============================================================
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public UserController(UserService service, PasswordEncoder passwordEncoder, UserRepository repository) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    //API uso administrativo

    @GetMapping
    public ResponseEntity<List<User>> listar() {
        log.info("👥 [GET] Listar usuarios");
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> obtener(@PathVariable Long id) {
        log.info("🔍 [GET] Obtener ID usuario: {}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<User> crear(@Valid @RequestBody User user) {
        log.info("📝 [POST] Crear usuario: {}", user.getEmail());
        User creado = service.crear(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<User> actualizar(@PathVariable Long id,
            @Valid @RequestBody User user) {
        log.info("✏️ [PUT] Actualizar usuario ID: {}", id);
        return ResponseEntity.ok(service.actualizar(id, user));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("🗑️ [DELETE] Eliminar usuario ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    //API adicionales

    @GetMapping("/email/{email}")
    public ResponseEntity<User> obtenerPorEmail(@PathVariable String email) {
        log.info("📧 [GET] Buscar por email: {}", email);
        return ResponseEntity.ok(service.buscarPorEmail(email));
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<User>> listarPorRol(@PathVariable String rol) {
        log.info("🎯 [GET] Usuarios por rol: {}", rol);
        List<User> usuarios = service.listarPorRol(rol);
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(usuarios);
    }

    //API uso FrontEnd

    @PostMapping("/register")
    public ResponseEntity<User> registrar(@Valid @RequestBody User user) {
        log.info("📝 [POST] Registro de usuario desde FrontEnd: {}", user.getEmail());
        User creado = service.registrarUsuario(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request) {
        log.info("🔐 [POST] Login para email: {}", request.getEmail());
        User user = service.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/recover/{email}")
    public ResponseEntity<String> recuperarPorEmail(@PathVariable String email) {
        log.info("📧 [GET] Recuperar usuario por email: {}", email);
        try {
            User user = service.buscarPorEmail(email);

            // Generar contraseña temporal (8 caracteres aleatorios)
            String tempPassword = UUID.randomUUID().toString().substring(0, 8);

            // Guardar hash de la contraseña temporal
            user.setPassword(passwordEncoder.encode(tempPassword));
            repository.save(user);

            // Retornar la contraseña temporal para mostrar en Angular
            return ResponseEntity.ok(tempPassword);

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<User> actualizarPerfil(@PathVariable Long id, @RequestBody User user) {
        log.info("👤 [PUT] Actualizar perfil de usuario ID: {}", id);
        return ResponseEntity.ok(service.actualizarPerfil(id, user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("🗑️ [DELETE] Eliminar usuario ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
