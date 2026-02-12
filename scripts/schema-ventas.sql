-- ============================================
-- BASE DE DATOS: ventas_db
-- MICROSERVICIO: ms-ventas
-- Archivo: scripts/schema-ventas.sql
-- ============================================

\c ventas_db;

-- ============================================
-- TIPOS ENUM
-- ============================================

CREATE TYPE estado_carrito_enum AS ENUM ('Activo', 'Completado', 'Abandonado');
CREATE TYPE tipo_entrega_enum AS ENUM ('Envio', 'Recojo en Tienda');
CREATE TYPE estado_pedido_enum AS ENUM ('Pendiente', 'Confirmado', 'En Preparacion', 'Enviado', 'Entregado', 'Cancelado');
CREATE TYPE estado_pago_enum AS ENUM ('Pendiente', 'Pagado', 'Fallido', 'Reembolsado');

-- ============================================
-- TABLAS
-- ============================================

-- Tabla de Carritos
CREATE TABLE carritos (
                          id_carrito SERIAL PRIMARY KEY,
                          id_usuario INT NOT NULL,
                          estado estado_carrito_enum DEFAULT 'Activo',
                          fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trigger para actualizar fecha
CREATE OR REPLACE FUNCTION actualizar_fecha_modificacion()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_actualizar_carritos
    BEFORE UPDATE ON carritos
    FOR EACH ROW
    EXECUTE FUNCTION actualizar_fecha_modificacion();

-- Tabla de Detalle del Carrito
CREATE TABLE detalle_carrito (
                                 id_detalle SERIAL PRIMARY KEY,
                                 id_carrito INT NOT NULL,
                                 id_producto INT NOT NULL,
                                 cantidad INT NOT NULL DEFAULT 1,
                                 precio_unitario DECIMAL(10, 2) NOT NULL,
                                 subtotal DECIMAL(10, 2) NOT NULL,
                                 fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (id_carrito) REFERENCES carritos(id_carrito) ON DELETE CASCADE
);

-- Tabla de Direcciones de Envío
CREATE TABLE direcciones_envio (
                                   id_direccion SERIAL PRIMARY KEY,
                                   id_usuario INT NOT NULL,
                                   nombre_destinatario VARCHAR(150) NOT NULL,
                                   telefono_destinatario VARCHAR(20) NOT NULL,
                                   departamento VARCHAR(100) NOT NULL,
                                   provincia VARCHAR(100) NOT NULL,
                                   distrito VARCHAR(100) NOT NULL,
                                   direccion_linea1 VARCHAR(255) NOT NULL,
                                   direccion_linea2 VARCHAR(255),
                                   referencia TEXT,
                                   codigo_postal VARCHAR(10),
                                   es_principal BOOLEAN DEFAULT FALSE,
                                   estado BOOLEAN DEFAULT TRUE,
                                   fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Métodos de Pago
CREATE TABLE metodos_pago (
                              id_metodo_pago SERIAL PRIMARY KEY,
                              nombre_metodo VARCHAR(100) NOT NULL,
                              descripcion TEXT,
                              icono_url VARCHAR(255),
                              estado BOOLEAN DEFAULT TRUE,
                              fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Pedidos
CREATE TABLE pedidos (
                         id_pedido SERIAL PRIMARY KEY,
                         numero_pedido VARCHAR(20) NOT NULL UNIQUE,
                         id_usuario INT NOT NULL,
                         id_direccion_envio INT NULL,
                         tipo_entrega tipo_entrega_enum NOT NULL,
                         id_sucursal_recojo INT NULL,
                         id_metodo_pago INT NOT NULL,
                         subtotal DECIMAL(10, 2) NOT NULL,
                         costo_envio DECIMAL(10, 2) DEFAULT 0.00,
                         descuento DECIMAL(10, 2) DEFAULT 0.00,
                         total DECIMAL(10, 2) NOT NULL,
                         estado_pedido estado_pedido_enum DEFAULT 'Pendiente',
                         estado_pago estado_pago_enum DEFAULT 'Pendiente',
                         notas TEXT,
                         fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (id_direccion_envio) REFERENCES direcciones_envio(id_direccion),
                         FOREIGN KEY (id_metodo_pago) REFERENCES metodos_pago(id_metodo_pago)
);

CREATE TRIGGER trigger_actualizar_pedidos
    BEFORE UPDATE ON pedidos
    FOR EACH ROW
    EXECUTE FUNCTION actualizar_fecha_modificacion();

-- Tabla de Detalle de Pedidos
CREATE TABLE detalle_pedido (
                                id_detalle_pedido SERIAL PRIMARY KEY,
                                id_pedido INT NOT NULL,
                                id_producto INT NOT NULL,
                                cantidad INT NOT NULL,
                                precio_unitario DECIMAL(10, 2) NOT NULL,
                                subtotal DECIMAL(10, 2) NOT NULL,
                                FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido) ON DELETE CASCADE
);

-- ============================================
-- ÍNDICES
-- ============================================

CREATE INDEX idx_carritos_usuario ON carritos(id_usuario);
CREATE INDEX idx_carritos_estado ON carritos(estado);
CREATE INDEX idx_detalle_carrito_carrito ON detalle_carrito(id_carrito);
CREATE INDEX idx_detalle_carrito_producto ON detalle_carrito(id_producto);
CREATE INDEX idx_pedidos_usuario ON pedidos(id_usuario);
CREATE INDEX idx_pedidos_estado ON pedidos(estado_pedido);
CREATE INDEX idx_pedidos_numero ON pedidos(numero_pedido);
CREATE INDEX idx_detalle_pedido_pedido ON detalle_pedido(id_pedido);
CREATE INDEX idx_detalle_pedido_producto ON detalle_pedido(id_producto);

-- ============================================
-- DATOS INICIALES
-- ============================================

-- Insertar Métodos de Pago
INSERT INTO metodos_pago (nombre_metodo, descripcion) VALUES
                                                          ('Tarjeta de Crédito/Débito', 'Visa, Mastercard, American Express'),
                                                          ('Yape', 'Pago mediante Yape'),
                                                          ('Plin', 'Pago mediante Plin'),
                                                          ('Transferencia Bancaria', 'Transferencia directa'),
                                                          ('Pago Contra Entrega', 'Pago en efectivo al recibir');

\echo '✅ Schema ventas_db creado exitosamente';
\echo '   - 5 Métodos de pago creados';
\echo '   - Tablas de carritos y pedidos listas';