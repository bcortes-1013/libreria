package com.fullstack.usuario.service;

import com.fullstack.exception.ResourceNotFoundException; // ya creada
import com.fullstack.usuario.model.Usuario;
import com.fullstack.usuario.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j; // Semana 2 → logging profesional
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ===============================================================
 * 📘 Clase: UsuarioService
 * ---------------------------------------------------------------
 * Capa de negocio para el microservicio Usuario.
 *
 * 🔹 Semana 2:
 * - Reglas de negocio:
 * 1) Unicidad de email (no permitir duplicados).
 * 2) Búsqueda con 404 cuando no exista.
 * - Logging con @Slf4j en operaciones clave.
 * ===============================================================
 */
@Slf4j
@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    // ============================================================
    // 🔸 CRUD básico (con logs) — Semana 2
    // ============================================================

    public List<Usuario> listar() {
        log.info("👥 Listando todos los usuarios");
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        log.info("🔍 Buscando usuario con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    /**
     * Crea un nuevo usuario aplicando reglas de negocio.
     * Reglas Semana 2:
     * - El email debe ser único.
     */
    public Usuario crear(Usuario u) {
        log.info("📝 Creando usuario: {}", u.getEmail());

        repository.findByEmail(u.getEmail()).ifPresent(existing -> {
            log.warn("⚠️ Intento de duplicar email: {}", u.getEmail());
            throw new IllegalArgumentException("El email ya está registrado");
        });

        Usuario guardado = repository.save(u);
        log.info("✅ Usuario creado con ID: {}", guardado.getId());
        return guardado;
    }

    /**
     * Actualiza un usuario existente.
     * Reglas:
     * - Si cambia el email, validar que no esté usado por otro registro.
     */
    public Usuario actualizar(Long id, Usuario data) {
        log.info("✏️ Actualizando usuario ID: {}", id);
        Usuario existente = buscarPorId(id);

        // Si el email cambia, validar unicidad
        if (!existente.getEmail().equalsIgnoreCase(data.getEmail())) {
            repository.findByEmail(data.getEmail()).ifPresent(other -> {
                log.warn("⚠️ Email ya registrado por otro usuario: {}", data.getEmail());
                throw new IllegalArgumentException("El email ya está registrado por otro usuario");
            });
        }

        existente.setNombre(data.getNombre());
        existente.setEmail(data.getEmail());
        existente.setTelefono(data.getTelefono());
        existente.setFechaRegistro(data.getFechaRegistro());
        existente.setRol(data.getRol());

        Usuario actualizado = repository.save(existente);
        log.info("✅ Usuario actualizado ID: {}", actualizado.getId());
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("🗑️ Eliminando usuario ID: {}", id);
        if (!repository.existsById(id)) {
            log.error("❌ No se puede eliminar. Usuario no existe: {}", id);
            throw new ResourceNotFoundException("Usuario no existe: " + id);
        }
        repository.deleteById(id);
        log.info("✅ Usuario eliminado ID: {}", id);
    }

    // ============================================================
    // 🔸 Consultas personalizadas (Semana 2 → complejidad)
    // ============================================================
    public Usuario buscarPorEmail(String email) {
        log.info("📧 Buscando usuario por email: {}", email);
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    public List<Usuario> listarPorRol(String rol) {
        log.info("🎯 Listando usuarios por rol: {}", rol);
        return repository.findByRol(rol);
    }
}
