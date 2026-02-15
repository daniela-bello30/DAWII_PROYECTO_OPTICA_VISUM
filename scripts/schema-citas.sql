
DROP TYPE IF EXISTS estado_cita_enum CASCADE;
CREATE TYPE estado_cita_enum AS ENUM ('Programada', 'Confirmada', 'Completada', 'Cancelada', 'No Asistio');


CREATE OR REPLACE FUNCTION actualizar_fecha_modificacion()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TABLE IF NOT EXISTS sucursales (
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

CREATE TABLE IF NOT EXISTS tipos_servicio (
                                              id_tipo_servicio SERIAL PRIMARY KEY,
                                              nombre_servicio VARCHAR(150) NOT NULL,
    descripcion TEXT,
    duracion_minutos INT NOT NULL DEFAULT 30,
    precio DECIMAL(10, 2) DEFAULT 0.00,
    estado BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS citas (
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


DROP TRIGGER IF EXISTS trigger_actualizar_citas ON citas;
CREATE TRIGGER trigger_actualizar_citas
    BEFORE UPDATE ON citas
    FOR EACH ROW
    EXECUTE FUNCTION actualizar_fecha_modificacion();


INSERT INTO tipos_servicio (nombre_servicio, descripcion, duracion_minutos, precio) VALUES
                                                                                        ('Examen de Vista', 'Evaluación completa de la visión', 30, 30.00),
                                                                                        ('Ajuste de Montura', 'Ajuste y calibración de monturas', 15, 0.00),
                                                                                        ('Limpieza de Lentes', 'Limpieza profunda profesional', 10, 0.00),
                                                                                        ('Graduación de Lentes', 'Medición para lentes de medida', 45, 50.00);


INSERT INTO sucursales (nombre_sucursal, direccion, departamento, provincia, distrito, telefono, email, horario_atencion) VALUES
                                                                                                                              ('VISUM - Centro de Lima', 'Jr. de la Unión 500', 'Lima', 'Lima', 'Lima', '(01) 234-5678', 'centrolima@visum.com',
                                                                                                                               '{"lunes": "9:00-20:00", "martes": "9:00-20:00", "miercoles": "9:00-20:00", "jueves": "9:00-20:00", "viernes": "9:00-20:00", "sabado": "9:00-20:00", "domingo": "10:00-18:00"}'::JSONB),
                                                                                                                              ('VISUM - Miraflores', 'Av. Larco 1234', 'Lima', 'Lima', 'Miraflores', '(01) 234-5679', 'miraflores@visum.com',
                                                                                                                               '{"lunes": "9:00-21:00", "martes": "9:00-21:00", "miercoles": "9:00-21:00", "jueves": "9:00-21:00", "viernes": "9:00-21:00", "sabado": "9:00-21:00", "domingo": "10:00-19:00"}'::JSONB),
                                                                                                                              ('VISUM - San Isidro', 'Av. Javier Prado 890', 'Lima', 'Lima', 'San Isidro', '(01) 234-5680', 'sanisidro@visum.com',
                                                                                                                               '{"lunes": "9:00-20:00", "martes": "9:00-20:00", "miercoles": "9:00-20:00", "jueves": "9:00-20:00", "viernes": "9:00-20:00", "sabado": "9:00-20:00", "domingo": "10:00-18:00"}'::JSONB);