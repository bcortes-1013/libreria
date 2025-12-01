// ============================================================
// Configuración Global de CORS (Cross-Origin Resource Sharing)
// ============================================================
//
// 📘 Contexto: Backend - Spring Boot
//
// Semana 3 - Configuración complementaria del microservicio
//
// Esta clase permite que otras aplicaciones (como un futuro frontend
// Angular o Postman) puedan consumir los endpoints REST del backend
// sin ser bloqueadas por las políticas de CORS del navegador.
//
// CORS (Cross-Origin Resource Sharing) es una medida de seguridad
// que impide que un dominio (por ejemplo localhost:4200) acceda a otro
// (por ejemplo localhost:8080) si el servidor no lo autoriza explícitamente.
//
// Con esta configuración global, todos los controladores bajo /api/**
// estarán habilitados para aceptar peticiones desde otros orígenes.
//
// ============================================================

package com.fullstack.libreria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 🔧 Indica que esta clase define configuración para Spring Boot
public class CorsConfig {

    // ============================================================
    // Método que define las reglas globales de CORS
    // ============================================================
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            // Este método sobreescribe la configuración por defecto de Spring
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                // 1️⃣ Se habilitan todas las rutas que empiecen con "/api/"
                registry.addMapping("/api/**")

                        // 2️⃣ Se permite el acceso desde cualquier origen
                        // (útil mientras desarrollamos; luego se puede restringir)
                        .allowedOrigins("http://localhost:4200")

                        // 3️⃣ Métodos HTTP permitidos
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

                        // 4️⃣ Cabeceras permitidas
                        .allowedHeaders("*")

                        // 5️⃣ Si fuera necesario, permitir el envío de credenciales (cookies, tokens,
                        // etc.)
                        .allowCredentials(false);
            }
        };
    }
}
