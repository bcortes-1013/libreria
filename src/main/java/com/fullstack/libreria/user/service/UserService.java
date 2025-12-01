package com.fullstack.libreria.user.service;

import com.fullstack.libreria.exception.ResourceNotFoundException;
import com.fullstack.libreria.user.model.User;
import com.fullstack.libreria.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
public class UserService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================================================
    // 🔸 CRUD básico (con logs) — Semana 2
    // ============================================================

    public List<User> listar() {
        log.info("👥 Listando todos los usuarios");
        return repository.findAll();
    }

    public User buscarPorId(Long id) {
        log.info("🔍 Buscando usuario con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    /**
     * Crea un nuevo usuario aplicando reglas de negocio.
     * Reglas Semana 2:
     * - El email debe ser único.
     */
    public User crear(User u) {
        log.info("📝 Creando usuario: {}", u.getEmail());

        repository.findByEmail(u.getEmail()).ifPresent(existing -> {
            log.warn("⚠️ Intento de duplicar email: {}", u.getEmail());
            throw new IllegalArgumentException("El email ya está registrado");
        });

        User guardado = repository.save(u);
        log.info("✅ Usuario creado con ID: {}", guardado.getId());
        return guardado;
    }

    /**
     * Actualiza un usuario existente.
     * Reglas:
     * - Si cambia el email, validar que no esté usado por otro registro.
     */
    public User actualizar(Long id, User data) {
        log.info("✏️ Actualizando usuario ID: {}", id);
        User existente = buscarPorId(id);

        // Si el email cambia, validar unicidad
        if (!existente.getEmail().equalsIgnoreCase(data.getEmail())) {
            repository.findByEmail(data.getEmail()).ifPresent(other -> {
                log.warn("⚠️ Email ya registrado por otro usuario: {}", data.getEmail());
                throw new IllegalArgumentException("El email ya está registrado por otro usuario");
            });
        }

        existente.setFullName(data.getFullName());
        existente.setEmail(data.getEmail());
        existente.setPhone(data.getPhone());
        existente.setRegisterDate(data.getRegisterDate());
        existente.setRol(data.getRol());

        User actualizado = repository.save(existente);
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
    // Consultas personalizadas
    // ============================================================
    public User buscarPorEmail(String email) {
        log.info("📧 Buscando usuario por email: {}", email);
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    public List<User> listarPorRol(String rol) {
        log.info("🎯 Listando usuarios por rol: {}", rol);
        return repository.findByRol(rol);
    }

    // ============================================================
    // Métodos específicos para autenticación y registro
    // ============================================================

    /**
     * Registro de usuario desde el FrontEnd.
     *
     * Regla:
     * - Todo usuario que se registra desde la aplicación web tendrá
     *   rol TECNICO por defecto.
     */
    public User registrarUsuario(User data) {
        log.info("📝 [Registro] Registrando nuevo usuario: {}", data.getEmail());

        validarEmailUnico(data.getEmail(), null);

        User nuevo = new User();
        nuevo.setFullName(data.getFullName());
        nuevo.setEmail(data.getEmail());
        // 🔐 Encriptar contraseña antes de guardar
        nuevo.setPassword(encoder.encode(data.getPassword()));
        nuevo.setPhone(data.getPhone());
        nuevo.setRegisterDate(LocalDate.now());
        nuevo.setRol(data.getRol()); // rol por defecto para registro web

        User guardado = repository.save(nuevo);
        log.info("✅ [Registro] Usuario registrado con ID: {}", guardado.getId());
        return guardado;
    }

    /**
     * Inicio de sesión sencillo.
     *
     * Flujo:
     * 1. Busca el usuario por email.
     * 2. Compara la contraseña enviada con la almacenada.
     * 3. Si no coincide, lanza IllegalArgumentException (se manejará
     *    en el controller / handler para devolver un 400 al FrontEnd).
     */
    public User login(String email, String password) {
        log.info("🔐 [Login] Intento de login con email: {}", email);

        User usuario = buscarPorEmail(email);

        // 🔐 Validar contraseña usando BCrypt
        if (!encoder.matches(password, usuario.getPassword())) {
            log.warn("❌ [Login] Contraseña incorrecta para email: {}", email);
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        log.info("✅ [Login] Usuario autenticado: {} con rol {}", usuario.getEmail(), usuario.getRol());
        return usuario;
    }

    /**
     * Actualización de perfil (nombre y teléfono principalmente).
     */
    public User actualizarPerfil(Long id, User data) {
        log.info("👤 [Perfil] Actualizando perfil del usuario ID: {}", id);
        User existente = buscarPorId(id);

        existente.setFullName(data.getFullName());
        existente.setPhone(data.getPhone());
        existente.setRol(data.getRol() != null ? data.getRol() : existente.getRol());

        if (data.getPassword() != null && !data.getPassword().isBlank()) {
            String hashed = passwordEncoder.encode(data.getPassword());
            existente.setPassword(hashed);
        }

        User actualizado = repository.save(existente);
        log.info("✅ [Perfil] Perfil actualizado ID: {}", actualizado.getId());
        return actualizado;
    }

    // ============================================================
    // Método de apoyo interno
    // ============================================================

    /**
     * Valida que el email no esté registrado por otro usuario.
     *
     * @param email   email a validar
     * @param idActual id del usuario actual (para actualizaciones),
     *                 puede ser null en creaciones.
     */
    private void validarEmailUnico(String email, Long idActual) {
        repository.findByEmail(email).ifPresent(existing -> {
            if (idActual == null || !existing.getId().equals(idActual)) {
                log.warn("⚠️ Email ya registrado: {}", email);
                throw new IllegalArgumentException("El email ya está registrado");
            }
        });
    }
}
