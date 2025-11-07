package com.fullstack.usuario.repository;

import com.fullstack.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * ===============================================================
 * 📘 Interfaz: UsuarioRepository
 * ---------------------------------------------------------------
 * Acceso a datos para la entidad Usuario (CRUD + consultas derivadas).
 *
 * 🔹 Semana 2:
 * - Se agregan métodos derivados para aumentar la complejidad:
 * findByEmail, findByRol.
 * ===============================================================
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por email (debe ser único).
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Lista usuarios por rol (ADMIN o ANALISTA).
     */
    List<Usuario> findByRol(String rol);
}
