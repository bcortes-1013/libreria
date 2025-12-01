package com.fullstack.libreria.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.libreria.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por email (debe ser único).
     */
    Optional<User> findByEmail(String email);

    /**
     * Lista usuarios por rol (ADMIN o TECNICO).
     */
    List<User> findByRol(String rol);
}
