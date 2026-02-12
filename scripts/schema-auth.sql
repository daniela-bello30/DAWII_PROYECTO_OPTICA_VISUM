-- ============================================
-- BASE DE DATOS: auth_db
-- MICROSERVICIO: ms-seguridad
-- Archivo: scripts/schema-auth.sql
-- ============================================

\c auth_db;

-- ============================================
-- TIPOS ENUM
-- ============================================

CREATE TYPE tipo_documento_enum AS ENUM ('DNI', 'Pasaporte', 'Carnet Extranjeria');

-- ============================================
-- TABLAS
-- ============================================

-- Tabla de Roles
CREATE TABLE roles (
                       id_rol SERIAL PRIMARY KEY,
                       nombre_rol VARCHAR(50) NOT NULL UNIQUE,
                       descripcion VARCHAR(255),
                       estado BOOLEAN DEFAULT TRUE,
                       fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Usuarios
CREATE TABLE usuarios (
                          id_usuario SERIAL PRIMARY KEY,
                          nombres VARCHAR(100) NOT NULL,
                          apellidos VARCHAR(100) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          telefono VARCHAR(20),
                          documento_identidad VARCHAR(20) UNIQUE,
                          tipo_documento tipo_documento_enum DEFAULT 'DNI',
                          fecha_nacimiento DATE,
                          id_rol INT NOT NULL,
                          estado BOOLEAN DEFAULT TRUE,
                          fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          ultimo_acceso TIMESTAMP NULL,
                          token_recuperacion VARCHAR(255) NULL,
                          token_expiracion TIMESTAMP NULL,
                          FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

-- Tabla de Refresh Tokens (Semana 5 - JWT)
CREATE TABLE refresh_tokens (
                                id_token SERIAL PRIMARY KEY,
                                token VARCHAR(500) NOT NULL UNIQUE,
                                id_usuario INT NOT NULL,
                                fecha_expiracion TIMESTAMP NOT NULL,
                                fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- ============================================
-- ÍNDICES
-- ============================================

CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_documento ON usuarios(documento_identidad);
CREATE INDEX idx_usuarios_rol ON usuarios(id_rol);
CREATE INDEX idx_refresh_tokens_usuario ON refresh_tokens(id_usuario);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);

-- ============================================
-- DATOS INICIALES
-- ============================================

-- Insertar Roles
INSERT INTO roles (nombre_rol, descripcion) VALUES
                                                ('ROLE_ADMIN', 'Administrador del sistema'),
                                                ('ROLE_CLIENTE', 'Cliente de la óptica'),
                                                ('ROLE_EMPLEADO', 'Empleado de la óptica');

-- Insertar Usuario Administrador
-- Password: admin123 (BCrypt hash)
INSERT INTO usuarios (nombres, apellidos, email, password, documento_identidad, id_rol) VALUES
    ('Admin', 'Sistema', 'admin@visum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J8bOFCTGZI4PvvLtGxQkLYLGZQkYpO', '12345678', 1);

-- Insertar Usuarios de Prueba
-- Todos con password: test123
INSERT INTO usuarios (nombres, apellidos, email, password, documento_identidad, tipo_documento, fecha_nacimiento, id_rol) VALUES
                                                                                                                              ('Juan', 'Pérez García', 'juan.perez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J8bOFCTGZI4PvvLtGxQkLYLGZQkYpO', '45678901', 'DNI', '1990-05-15', 2),
                                                                                                                              ('María', 'López Torres', 'maria.lopez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J8bOFCTGZI4PvvLtGxQkLYLGZQkYpO', '56789012', 'DNI', '1992-08-20', 2),
                                                                                                                              ('Carlos', 'Rodríguez Sánchez', 'carlos.rodriguez@visum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J8bOFCTGZI4PvvLtGxQkLYLGZQkYpO', '67890123', 'DNI', '1988-03-10', 3);

\echo '✅ Schema auth_db creado exitosamente';
\echo '   - 3 Roles creados';
\echo '   - 4 Usuarios creados (1 admin + 3 prueba)';