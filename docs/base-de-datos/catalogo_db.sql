-- SCRIPT SQL CORREGIDO PARA CATALOGO_DB

DROP TABLE IF EXISTS imagenes_producto CASCADE;
DROP TABLE IF EXISTS productos CASCADE;
DROP TABLE IF EXISTS categorias CASCADE;
DROP TABLE IF EXISTS marcas CASCADE;
DROP TYPE IF EXISTS genero_enum CASCADE;

-- Tabla de Categorías
CREATE TABLE categorias (
    id_categoria SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,           -- ← CAMBIO: nombre_categoria → nombre
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE,            -- ← CAMBIO: estado → activo
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Marcas
CREATE TABLE marcas (
    id_marca SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,    -- ← CAMBIO: nombre_marca → nombre
    pais_origen VARCHAR(100),               -- ← NUEVO: país de origen
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE,            -- ← CAMBIO: estado → activo
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Productos
CREATE TABLE productos (
    id_producto SERIAL PRIMARY KEY,
    sku VARCHAR(50) UNIQUE,                 -- ← NUEVO: SKU del producto
    nombre VARCHAR(200) NOT NULL,           -- ← CAMBIO: nombre_producto → nombre
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,          -- ← CAMBIO: precio_unitario → precio
    stock INT NOT NULL DEFAULT 0,
    id_categoria INT NOT NULL,
    id_marca INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,            -- ← CAMBIO: estado → activo
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria),
    CONSTRAINT fk_producto_marca FOREIGN KEY (id_marca) REFERENCES marcas(id_marca)
);

-- Trigger para actualizar fecha_actualizacion
CREATE OR REPLACE FUNCTION actualizar_fecha_modificacion()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_actualizar_productos
BEFORE UPDATE ON productos
FOR EACH ROW
EXECUTE FUNCTION actualizar_fecha_modificacion();

-- Tabla de Imágenes
CREATE TABLE imagenes_producto (
    id_imagen SERIAL PRIMARY KEY,
    id_producto INT NOT NULL,
    url_imagen VARCHAR(255) NOT NULL,
    es_principal BOOLEAN DEFAULT FALSE,
    orden INT DEFAULT 0,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_imagen_producto FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_productos_categoria ON productos(id_categoria);
CREATE INDEX idx_productos_marca ON productos(id_marca);
CREATE INDEX idx_productos_precio ON productos(precio);
CREATE INDEX idx_productos_activo ON productos(activo);
CREATE INDEX idx_productos_stock ON productos(stock);
CREATE INDEX idx_productos_nombre ON productos(nombre);
CREATE INDEX idx_marcas_nombre ON marcas(nombre);
CREATE INDEX idx_categorias_nombre ON categorias(nombre);

-- Datos Iniciales - Categorías
INSERT INTO categorias (nombre, descripcion) VALUES
('Monturas Ópticas', 'Monturas para lentes de medida'),
('Lentes de Sol', 'Gafas de sol con protección UV'),
('Lentes de Contacto', 'Lentes de contacto blandos y rígidos'),
('Accesorios', 'Estuches, paños y productos de limpieza');

-- Datos Iniciales - Marcas
INSERT INTO marcas (nombre, pais_origen, descripcion) VALUES
('Ray-Ban', 'Estados Unidos', 'Marca icónica de gafas de sol'),
('Oakley', 'Estados Unidos', 'Gafas deportivas de alta calidad'),
('Prada', 'Italia', 'Diseño italiano de lujo'),
('Guess', 'Estados Unidos', 'Moda y estilo contemporáneo'),
('Arnette', 'Estados Unidos', 'Estilo urbano y juvenil');

-- Datos Iniciales - Productos
INSERT INTO productos (sku, nombre, descripcion, precio, stock, id_categoria, id_marca) VALUES
('RB-5228-BLK', 'Ray-Ban RX5228', 'Montura clásica de acetato con diseño atemporal', 450.00, 15, 1, 1),
('OK-CROSS-GRY', 'Oakley Crosslink', 'Montura deportiva ligera y resistente', 520.00, 8, 1, 2),
('PR-16M-TORT', 'Prada VPR 16M', 'Elegancia italiana en cada detalle', 890.00, 5, 1, 3),
('RB-AVI-GLD', 'Ray-Ban Aviator Classic', 'Los icónicos lentes aviador', 650.00, 25, 2, 1),
('OK-HOLB-BLK', 'Oakley Holbrook', 'Diseño retro con tecnología moderna', 720.00, 10, 2, 2);

-- Datos Iniciales - Imágenes
INSERT INTO imagenes_producto (id_producto, url_imagen, es_principal, orden) VALUES
(1, 'https://via.placeholder.com/400x300?text=Ray-Ban+RX5228', TRUE, 1),
(2, 'https://via.placeholder.com/400x300?text=Oakley+Crosslink', TRUE, 1),
(3, 'https://via.placeholder.com/400x300?text=Prada+VPR+16M', TRUE, 1),
(4, 'https://via.placeholder.com/400x300?text=Ray-Ban+Aviator', TRUE, 1),
(5, 'https://via.placeholder.com/400x300?text=Oakley+Holbrook', TRUE, 1);

-- Verificar datos
SELECT * FROM categorias;
SELECT * FROM marcas;
SELECT * FROM productos;