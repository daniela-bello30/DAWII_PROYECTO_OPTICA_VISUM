-- ============================================
-- BD CITAS: CREAR LA BD Y LAS TABLAS SE AUTOGENERAN DESDE EL CODIGO, SOLO INSERTAR ESTE SCRIPT PARA PRUEBAS
-- ============================================

INSERT INTO sucursales (
    departamento, 
    direccion, 
    distrito, 
    email, 
    estado, 
    fecha_creacion, 
    horario_atencion, 
    latitud, 
    longitud, 
    nombre_sucursal, 
    provincia, 
    telefono
) VALUES (
    'Lima', 
    'Av. Arequipa 1500', 
    'Lince', 
    'lince@visumoptica.com', 
    true, 
    CURRENT_TIMESTAMP, 
    'Lunes a Sábado: 9:00 AM - 8:00 PM', 
    -12.08333300, 
    -77.03333300, 
    'Visum Óptica - Sede Lince', 
    'Lima', 
    '01-4445555'
),
(
    'Lima', 
    'Av. José Pardo 432', 
    'Miraflores', 
    'miraflores@visumoptica.com', 
    true, 
    CURRENT_TIMESTAMP, 
    'Lunes a Domingo: 10:00 AM - 9:00 PM', 
    -12.11944400, 
    -77.03250000, 
    'Visum Óptica - Sede Miraflores', 
    'Lima', 
    '01-6667777'
);


INSERT INTO tipos_servicio (
    nombre_servicio, 
    descripcion, 
    duracion_minutos, 
    precio, 
    estado, 
    fecha_creacion
) VALUES (
    'Examen Visual Computarizado', 
    'Medición de vista con equipos de última generación.', 
    30, 
    0.00, 
    true, 
    CURRENT_TIMESTAMP
),
(
    'Adaptación de Lentes de Contacto', 
    'Prueba de tolerancia y medida exacta para uso de lentes de contacto.', 
    45, 
    50.00, 
    true, 
    CURRENT_TIMESTAMP
);

