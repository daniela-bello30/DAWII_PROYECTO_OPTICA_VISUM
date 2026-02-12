-- ============================================
-- BASE DE DATOS: catalogo_db
-- MICROSERVICIO: ms-catalogo
-- Archivo: scripts/schema-catalogo.sql
-- ============================================

\c catalogo_db;

-- ============================================
-- TIPOS ENUM
-- ============================================

CREATE TYPE genero_enum AS ENUM ('Hombre', 'Mujer', 'Unisex');

-- ============================================
-- TABLAS
-- ============================================

-- Tabla de Categorías
CREATE TABLE categorias (
                            id_categoria SERIAL PRIMARY KEY,
                            nombre_categoria VARCHAR(100) NOT NULL,
                            descripcion TEXT,
                            imagen_url VARCHAR(255),
                            estado BOOLEAN DEFAULT TRUE,
                            fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Marcas
CREATE TABLE marcas (
                        id_marca SERIAL PRIMARY KEY,
                        nombre_marca VARCHAR(100) NOT NULL UNIQUE,
                        descripcion TEXT,
                        logo_url VARCHAR(255),
                        estado BOOLEAN DEFAULT TRUE,
                        fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Productos
CREATE TABLE productos (
                           id_producto SERIAL PRIMARY KEY,
                           nombre_producto VARCHAR(200) NOT NULL,
                           descripcion TEXT,
                           id_categoria INT NOT NULL,
                           id_marca INT NOT NULL,
                           precio_unitario DECIMAL(10, 2) NOT NULL,
                           precio_descuento DECIMAL(10, 2) NULL,
                           stock INT NOT NULL DEFAULT 0,
                           color VARCHAR(50),
                           forma VARCHAR(50),
                           material VARCHAR(100),
                           genero genero_enum DEFAULT 'Unisex',
                           es_destacado BOOLEAN DEFAULT FALSE,
                           es_nuevo BOOLEAN DEFAULT FALSE,
                           en_promocion BOOLEAN DEFAULT FALSE,
                           estado BOOLEAN DEFAULT TRUE,
                           fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria),
                           FOREIGN KEY (id_marca) REFERENCES marcas(id_marca)
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

-- Tabla de Imágenes de Productos
CREATE TABLE imagenes_producto (
                                   id_imagen SERIAL PRIMARY KEY,
                                   id_producto INT NOT NULL,
                                   url_imagen VARCHAR(255) NOT NULL,
                                   es_principal BOOLEAN DEFAULT FALSE,
                                   orden INT DEFAULT 0,
                                   fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE
);

-- Tabla de Banners/Promociones
CREATE TABLE banners (
                         id_banner SERIAL PRIMARY KEY,
                         titulo VARCHAR(200) NOT NULL,
                         descripcion TEXT,
                         imagen_url VARCHAR(255) NOT NULL,
                         enlace VARCHAR(255),
                         orden INT DEFAULT 0,
                         estado BOOLEAN DEFAULT TRUE,
                         fecha_inicio DATE,
                         fecha_fin DATE,
                         fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- ÍNDICES
-- ============================================

CREATE INDEX idx_productos_categoria ON productos(id_categoria);
CREATE INDEX idx_productos_marca ON productos(id_marca);
CREATE INDEX idx_productos_precio ON productos(precio_unitario);
CREATE INDEX idx_productos_estado ON productos(estado);
CREATE INDEX idx_productos_stock ON productos(stock);

-- Índice para búsqueda full-text
CREATE INDEX idx_productos_busqueda ON productos
    USING GIN (to_tsvector('spanish', nombre_producto || ' ' || COALESCE(descripcion, '')));

-- ============================================
-- DATOS INICIALES
-- ============================================

-- Insertar Categorías
INSERT INTO categorias (nombre_categoria, descripcion) VALUES
                                                           ('Monturas Ópticas', 'Monturas para lentes de medida'),
                                                           ('Lentes de Sol', 'Gafas de sol con protección UV'),
                                                           ('Lentes de Contacto', 'Lentes de contacto blandos y rígidos'),
                                                           ('Accesorios', 'Estuches, paños y productos de limpieza');

-- Insertar Marcas
INSERT INTO marcas (nombre_marca, descripcion) VALUES
                                                   ('Ray-Ban', 'Marca icónica de gafas de sol'),
                                                   ('Oakley', 'Gafas deportivas de alta calidad'),
                                                   ('Prada', 'Diseño italiano de lujo'),
                                                   ('Guess', 'Moda y estilo contemporáneo'),
                                                   ('Arnette', 'Estilo urbano y juvenil');

-- Insertar Productos de Prueba
INSERT INTO productos (nombre_producto, descripcion, id_categoria, id_marca, precio_unitario, precio_descuento, stock, color, forma, material, genero, es_destacado, es_nuevo, en_promocion) VALUES
                                                                                                                                                                                                 ('Ray-Ban RX5228', 'Montura clásica de acetato con diseño atemporal', 1, 1, 450.00, 380.00, 15, 'Negro', 'Rectangular', 'Acetato', 'Unisex', true, false, true),
                                                                                                                                                                                                 ('Oakley Crosslink', 'Montura deportiva ligera y resistente', 1, 2, 520.00, NULL, 8, 'Gris', 'Rectangular', 'O-Matter', 'Hombre', true, true, false),
                                                                                                                                                                                                 ('Prada VPR 16M', 'Elegancia italiana en cada detalle', 1, 3, 890.00, 750.00, 5, 'Marrón Tortuga', 'Cat-Eye', 'Acetato', 'Mujer', true, false, true),
                                                                                                                                                                                                 ('Ray-Ban Aviator Classic', 'Los icónicos lentes aviador', 2, 1, 650.00, 550.00, 25, 'Dorado/Verde', 'Aviador', 'Metal', 'Unisex', true, false, true),
                                                                                                                                                                                                 ('Oakley Holbrook', 'Diseño retro con tecnología moderna', 2, 2, 720.00, 650.00, 10, 'Negro Mate', 'Cuadrada', 'O-Matter', 'Hombre', true, true, true);

-- Insertar Imágenes de Productos
INSERT INTO imagenes_producto (id_producto, url_imagen, es_principal, orden) VALUES
                                                                                 (1, 'https://via.placeholder.com/400x300?text=Ray-Ban+RX5228', true, 1),
                                                                                 (1, 'https://via.placeholder.com/400x300?text=Ray-Ban+RX5228+2', false, 2),
                                                                                 (2, 'https://via.placeholder.com/400x300?text=Oakley+Crosslink', true, 1),
                                                                                 (3, 'https://via.placeholder.com/400x300?text=Prada+VPR+16M', true, 1),
                                                                                 (4, 'https://via.placeholder.com/400x300?text=Ray-Ban+Aviator', true, 1),
                                                                                 (5, 'https://via.placeholder.com/400x300?text=Oakley+Holbrook', true, 1);

\echo '✅ Schema catalogo_db creado exitosamente';
\echo '   - 4 Categorías creadas';
\echo '   - 5 Marcas creadas';
\echo '   - 5 Productos de prueba creados';
\echo '   - 6 Imágenes de productos creadas';