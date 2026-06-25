DROP TABLE IF EXISTS refresh_tokens;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS roles;
DROP TYPE IF EXISTS tipo_documento_enum;

-- 1. Crear tabla de Roles
CREATE TABLE roles (
    id_rol SERIAL PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    estado BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Crear tabla de Usuarios 
-- Nota: tipo_documento se define como VARCHAR para evitar errores de casting con Java
CREATE TABLE usuarios (
    id_usuario SERIAL PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    documento_identidad VARCHAR(20) UNIQUE,
    tipo_documento VARCHAR(50) DEFAULT 'DNI', 
    fecha_nacimiento DATE,
    id_rol INT NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso TIMESTAMP NULL,
    token_recuperacion VARCHAR(255) NULL,
    token_expiracion TIMESTAMP NULL,
    CONSTRAINT fk_rol FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

-- 3. Crear tabla de Refresh Tokens
CREATE TABLE refresh_tokens (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT NOT NULL,
    CONSTRAINT fk_usuario_token FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);


INSERT INTO roles (nombre_rol, descripcion) VALUES
('ADMIN', 'Administrador total del sistema'),
('MEDICO', 'Personal médico y especialistas'),
('PACIENTE', 'Usuarios que reciben atención'),
('RECEPCIONISTA', 'Personal de atención al cliente');

-- PASO 2: Usuarios de prueba
-- Contraseña para todos los de prueba: admin123 
-- (Usando el hash que confirmaste: $2a$10$ftSBqaG7HXh4t7wS0I4MbuZg3/EYPuC/ll/gfMs9kBMo7A5dAScRu)

INSERT INTO usuarios 
(nombres, apellidos, email, password, telefono, documento_identidad, tipo_documento, fecha_nacimiento, id_rol)
VALUES
-- El ADMIN principal solicitado
('Test', 'Admin', 'testadmin@demo.com', '$2a$10$ftSBqaG7HXh4t7wS0I4MbuZg3/EYPuC/ll/gfMs9kBMo7A5dAScRu', '900000000', '99999999', 'DNI', '1990-01-01', 1),

-- Médicos
('Roberto', 'Sánchez', 'roberto.med@hospital.com', '$2a$10$ftSBqaG7HXh4t7wS0I4MbuZg3/EYPuC/ll/gfMs9kBMo7A5dAScRu', '988777666', '44556677', 'DNI', '1980-05-12', 2),
('Laura', 'Mendoza', 'laura.m@hospital.com', '$2a$10$ftSBqaG7HXh4t7wS0I4MbuZg3/EYPuC/ll/gfMs9kBMo7A5dAScRu', '911222333', '11223344', 'PASAPORTE', '1988-08-25', 2),

-- Pacientes
('Ana', 'Torres', 'ana.paciente@gmail.com', '$2a$10$ftSBqaG7HXh4t7wS0I4MbuZg3/EYPuC/ll/gfMs9kBMo7A5dAScRu', '955444333', '77889900', 'DNI', '1995-12-10', 3),
('Carlos', 'Ruiz', 'cruiz@outlook.com', '$2a$10$ftSBqaG7HXh4t7wS0I4MbuZg3/EYPuC/ll/gfMs9kBMo7A5dAScRu', '944333222', '88776655', 'CARNET_EXTRANJERIA', '1992-03-15', 3),
('Elena', 'Gómez', 'egomez@demo.com', '$2a$10$ftSBqaG7HXh4t7wS0I4MbuZg3/EYPuC/ll/gfMs9kBMo7A5dAScRu', '933222111', '55443322', 'DNI', '2000-01-20', 3),

-- Recepción
('Marcos', 'León', 'marcos.recep@hospital.com', '$2a$10$ftSBqaG7HXh4t7wS0I4MbuZg3/EYPuC/ll/gfMs9kBMo7A5dAScRu', '922111000', '12121212', 'DNI', '1994-06-30', 4);

SELECT email, password FROM usuarios WHERE email = 'testadmin@demo.com';
SELECT COUNT(*) FROM roles;  

-- ROLES
INSERT INTO roles (id, nombre, descripcion, estado) VALUES
(1, 'ADMIN', 'Administrador con acceso total al sistema', true),
(2, 'USER', 'Usuario regular con permisos básicos', true),
(3, 'DOCTOR', 'Especialista en salud visual (optometría/oftalmología)', true),
(4, 'PACIENTE', 'Paciente del sistema de la óptica', true);

-- USUARIOS
INSERT INTO usuarios (
    id,
    nombres,
    apellidos,
    email,
    password,
    telefono,
    documento,
    tipo_documento,
    fecha_nacimiento,
    id_rol,
    estado,
    fecha_registro,
    ultimo_acceso
) VALUES

(1,'María','García Admin','admin@cibertec.pe',
'$2a$10$N9qo8uLOickgx2ZMRZo5e.ez1k2kG0q7uC18m0DutLCRa9Q6P9e8e',
'999111222','10000001','DNI','1985-03-15',1,true,now(),now()),

(2,'Juan Carlos','Pérez López','juan@cibertec.pe',
'$2a$10$KbQiK8WmZ0pniS3pSkeY0.4Zz7z5tMZgJY6wzQYpK0eE8rN4QyY7a',
'988222333','10000002','DNI','1998-07-20',2,true,now(),now()),

(3,'Roberto','Sánchez Medina','doctor@cibertec.pe',
'$2a$10$1pHnQ2vQZqGJtKQxTQ5L2e8P0FqYt9Gz1vLk7VYz3G3QkZ5mR8q4G',
'977333444','10000003','DNI','1975-11-02',3,true,now(),now()),

(4,'Ana María','Torres Ramos','paciente@cibertec.pe',
'$2a$10$0uGQpJwQ9YyN2xL9WZQwOeU6P8JZK3X3FZ2tQe4U6pZ8k1Lx7Jk2W',
'966444555','10000004','DNI','2002-05-10',4,true,now(),now()),

(5,'Carlos','Mendoza Silva','carlos@cibertec.pe',
'$2a$10$KbQiK8WmZ0pniS3pSkeY0.4Zz7z5tMZgJY6wzQYpK0eE8rN4QyY7a',
'955666777','10000005','DNI','1995-09-18',2,true,now(),now());

-- ROLES
INSERT INTO roles (id, nombre, descripcion, estado) VALUES
(1, 'ADMIN', 'Administrador con acceso total al sistema', true),
(2, 'USER', 'Usuario regular con permisos básicos', true),
(3, 'DOCTOR', 'Especialista en salud visual (optometría/oftalmología)', true),
(4, 'PACIENTE', 'Paciente del sistema de la óptica', true);

-- USUARIOS
INSERT INTO usuarios (
    id,
    nombres,
    apellidos,
    email,
    password,
    telefono,
    documento,
    tipo_documento,
    fecha_nacimiento,
    id_rol,
    estado,
    fecha_registro,
    ultimo_acceso
) VALUES

(1,'María','García Admin','admin@cibertec.pe',
'$2a$10$N9qo8uLOickgx2ZMRZo5e.ez1k2kG0q7uC18m0DutLCRa9Q6P9e8e',
'999111222','10000001','DNI','1985-03-15',1,true,now(),now()),

(2,'Juan Carlos','Pérez López','juan@cibertec.pe',
'$2a$10$KbQiK8WmZ0pniS3pSkeY0.4Zz7z5tMZgJY6wzQYpK0eE8rN4QyY7a',
'988222333','10000002','DNI','1998-07-20',2,true,now(),now()),

(3,'Roberto','Sánchez Medina','doctor@cibertec.pe',
'$2a$10$1pHnQ2vQZqGJtKQxTQ5L2e8P0FqYt9Gz1vLk7VYz3G3QkZ5mR8q4G',
'977333444','10000003','DNI','1975-11-02',3,true,now(),now()),

(4,'Ana María','Torres Ramos','paciente@cibertec.pe',
'$2a$10$0uGQpJwQ9YyN2xL9WZQwOeU6P8JZK3X3FZ2tQe4U6pZ8k1Lx7Jk2W',
'966444555','10000004','DNI','2002-05-10',4,true,now(),now()),

(5,'Carlos','Mendoza Silva','carlos@cibertec.pe',
'$2a$10$KbQiK8WmZ0pniS3pSkeY0.4Zz7z5tMZgJY6wzQYpK0eE8rN4QyY7a',
'955666777','10000005','DNI','1995-09-18',2,true,now(),now());

SELECT * FROM roles;
SELECT * FROM usuarios;