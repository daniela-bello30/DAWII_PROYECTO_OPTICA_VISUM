#!/bin/bash
# ============================================
# Script para crear múltiples bases de datos
# Archivo: scripts/init-databases.sh
# ============================================

set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- Crear bases de datos
    CREATE DATABASE auth_db;
    CREATE DATABASE catalogo_db;
    CREATE DATABASE ventas_db;
    CREATE DATABASE citas_db;

    -- Otorgar privilegios
    GRANT ALL PRIVILEGES ON DATABASE auth_db TO postgres;
    GRANT ALL PRIVILEGES ON DATABASE catalogo_db TO postgres;
    GRANT ALL PRIVILEGES ON DATABASE ventas_db TO postgres;
    GRANT ALL PRIVILEGES ON DATABASE citas_db TO postgres;

    \echo '✅ Bases de datos creadas exitosamente'
    \echo '   - auth_db'
    \echo '   - catalogo_db'
    \echo '   - ventas_db'
    \echo '   - citas_db'
EOSQL