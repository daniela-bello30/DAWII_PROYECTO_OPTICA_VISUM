-- ============================================
-- BASE DE DATOS: citas_db
-- MICROSERVICIO: ms-citas
-- Archivo: scripts/schema-citas.sql
-- ============================================

\c citas_db;

-- ============================================
-- TIPOS ENUM
-- ============================================

CREATE TYPE estado_cita_enum AS ENUM ('Programada', 'Confirmada', 'Completada', 'Cancelada', 'No Asistio');

-- ============================================
-- TABLAS
-- ============================================

-- Tabla de Sucursales
CREATE TABLE sucursales (
                            id_sucursal SERIAL PRIMARY KEY,
                            nombre_sucursal VARCHAR(150) NOT NULL,
                            direccion VARCHAR(255) NOT NULL,
                            departamento VARCHAR(100) NOT NULL,
                            provincia VARCHAR(100) NOT NULL,
                            distrito VARCHAR(100) NOT NULL,
                            telefono VARCHAR(20),
                            email VARCHAR(150),
                            latitud DECIMAL(10, 8),
                            longitud DECIMAL(11, 8),
                            horario_atencion JSONB,
                            estado BOOLEAN DEFAULT TRUE,
                            fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Tipos de Servicios
CREATE TABLE tipos_servicio (
                                id_tipo_servicio SERIAL PRIMARY KEY,
                                nombre_servicio VARCHAR(150) NOT NULL,
                                descripcion TEXT,
                                duracion_minutos INT NOT NULL DEFAULT 30,
                                precio DECIMAL(10, 2) DEFAULT 0.00,
                                estado BOOLEAN DEFAULT TRUE,
                                fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Citas
CREATE TABLE citas (
                       id_cita SERIAL PRIMARY KEY,
                       numero_cita VARCHAR(20) NOT NULL UNIQUE,
                       id_usuario INT NOT NULL,
                       id_sucursal INT NOT NULL,
                       id_tipo_servicio INT NOT NULL,
                       fecha_cita DATE NOT NULL,
                       hora_cita TIME NOT NULL,
                       estado_cita estado_cita_enum DEFAULT 'Programada',
                       notas TEXT,
                       fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (id_sucursal) REFERENCES sucursales(id_sucursal),
                       FOREIGN KEY (id_tipo_servicio) REFERENCES tipos_servicio(id_tipo_servicio)
);

-- Trigger para actualizar fecha
CREATE OR REPLACE FUNCTION actualizar_fecha_modificacion()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_actualizar_citas
    BEFORE UPDATE ON citas
    FOR EACH ROW
    EXECUTE FUNCTION actualizar_fecha_modificacion();

-- ============================================
-- ÍNDICES
-- ============================================

CREATE INDEX idx_citas_usuario ON citas(id_usuario);
CREATE INDEX idx_citas_sucursal ON citas(id_sucursal);
CREATE INDEX idx_citas_fecha ON citas(fecha_cita);
CREATE INDEX idx_citas_numero ON citas(numero_cita);
CREATE INDEX idx_citas_estado ON citas(estado_cita);
CREATE INDEX idx_sucursales_estado ON sucursales(estado);

-- ============================================
-- DATOS INICIALES
-- ============================================

-- Insertar Tipos de Servicio
INSERT INTO tipos_servicio (nombre_servicio, descripcion, duracion_minutos, precio) VALUES
                                                                                        ('Examen de Vista', 'Evaluación completa de la visión', 30, 30.00),
                                                                                        ('Ajuste de Montura', 'Ajuste y calibración de monturas', 15, 0.00),
                                                                                        ('Limpieza de Lentes', 'Limpieza profunda profesional', 10, 0.00),
                                                                                        ('Graduación de Lentes', 'Medición para lentes de medida', 45, 50.00);

-- Insertar Sucursales
INSERT INTO sucursales (nombre_sucursal, direccion, departamento, provincia, distrito, telefono, email, horario_atencion) VALUES
                                                                                                                              ('VISUM - Centro de Lima', 'Jr. de la Unión 500', 'Lima', 'Lima', 'Lima', '(01) 234-5678', 'centrolima@visum.com',
                                                                                                                               '{"lunes": "9:00-20:00", "martes": "9:00-20:00", "miercoles": "9:00-20:00", "jueves": "9:00-20:00", "viernes": "9:00-20:00", "sabado": "9:00-20:00", "domingo": "10:00-18:00"}'::JSONB),
                                                                                                                              ('VISUM - Miraflores', 'Av. Larco 1234', 'Lima', 'Lima', 'Miraflores', '(01) 234-5679', 'miraflores@visum.com',
                                                                                                                               '{"lunes": "9:00-21:00", "martes": "9:00-21:00", "miercoles": "9:00-21:00", "jueves": "9:00-21:00", "viernes": "9:00-21:00", "sabado": "9:00-21:00", "domingo": "10:00-19:00"}'::JSONB),
                                                                                                                              ('VISUM - San Isidro', 'Av. Javier Prado 890', 'Lima', 'Lima', 'San Isidro', '(01) 234-5680', 'sanisidro@visum.com',
                                                                                                                               '{"lunes": "9:00-20:00", "martes": "9:00-20:00", "miercoles": "9:00-20:00", "jueves": "9:00-20:00", "viernes": "9:00-20:00", "sabado": "9:00-20:00", "domingo": "10:00-18:00"}'::JSONB);

\echo '✅ Schema citas_db creado exitosamente';
\echo '   - 4 Tipos de servicio creados';
\echo '   - 3 Sucursales creadas';