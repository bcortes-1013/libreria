# 📚 API REST - Librería

Este proyecto corresponde al desarrollo de una **API REST de pruebas** para gestionar libros.  
Forma parte del módulo de integración **Spring Boot + Angular** (Semana 3).

---

## 🚀 Tecnologías utilizadas

- **Java 17+**
- **Spring Boot 3**
- **Spring Data JPA**
- **H2 Database / PostgreSQL**
- **Maven**
- **RESTful API**

---

## ⚙️ Configuración rápida

1. Clonar el repositorio:
- git clone https://github.com/usuario/libreria.git
- cd libreria

2. Ejecutar el proyecto con Maven:
- mvn spring-boot:run

3. Acceder al backend:
- http://localhost:8080/api/libros

---

## 🔗 Endponits

- GET	/api/libros	Obtiene todos los libros
- GET	/api/libros/{id}	Obtiene un libro por su ID
- POST	/api/libros	Crea un nuevo libro
- PUT	/api/libros/{id}	Actualiza un libro existente
- DELETE	/api/libros/{id}	Elimina un libro