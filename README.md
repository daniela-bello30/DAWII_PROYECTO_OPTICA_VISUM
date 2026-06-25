# Sistema Óptica Visum 👓🚀
### Escuela de Tecnologías de la Información | CIBERTEC
**Carrera:** Computación e Informática  
**Curso:** Desarrollo de Aplicaciones Web II (6to Ciclo - Período 2026)  

---

## 📝 Resumen del Proyecto
El **Sistema Óptica Visum** es una solución de gestión integral de nivel empresarial diseñada para modernizar la operación y el canal digital de ópticas en el mercado peruano. La plataforma automatiza desde el agendamiento de citas oftalmológicas en tiempo real hasta el flujo completo de comercio electrónico (catálogo, carrito de compras, direcciones y pasarela de pagos)[cite: 1].

Desarrollado bajo un enfoque moderno de **Arquitectura de Microservicios**, el sistema garantiza alta disponibilidad, tolerancia a fallos, desacoplamiento y escalabilidad elástica[cite: 1].

---

## 🏗️ Arquitectura General del Sistema
El ecosistema está compuesto por **7 servicios distribuidos** independientes organizados bajo el patrón *Database per Service*[cite: 1], comunicados de forma síncrona mediante **Feign Clients** y asíncrona/reactiva mediante un Message Broker con **RabbitMQ**[cite: 1].

Para una referencia visual detallada del flujo de peticiones HTTPS, filtros de autenticación y la topología de la red, consulte el diagrama de arquitectura en el archivo adjunto `image_83b237.png`[cite: 1].

### Capa de Infraestructura
*   **Service Registry (Eureka Server - Puerto 8761):** Registro centralizado que gestiona el descubrimiento dinámico y el monitoreo de estado (*health checks*) de cada microservicio.
*   **Config Server (Puerto 8888):** Centralización de propiedades de entorno (`.yml`) conectadas a un repositorio Git con soporte para refresco dinámico en caliente.
*   **API Gateway (Puerto 8080):** Punto de entrada único encargado del enrutamiento inteligente, balanceo de carga, resolución de CORS y filtrado/validación estricta de tokens JWT.

### Capa de Microservicios de Negocio
1.  **MS-Seguridad (Puerto 8084):** Control de acceso RBAC (Roles: `ADMIN`, `MEDICO`, `PACIENTE`, `RECEPCIONISTA`). Autenticación robusta mediante tokens JWT con persistencia y rotación de *Refresh Tokens* e inyección de algoritmos de hashing BCrypt[cite: 1].
2.  **MS-Catálogo (Puerto 8081):** Motor de gestión de inventario, marcas y categorías. Incorpora índices optimizados para búsquedas avanzadas con filtros múltiples y un sistema elástico de control de stock[cite: 1].
3.  **MS-Ventas (Puerto 8082):** Orquestador del carrito de compras dinámico y procesamiento transaccional de pedidos. Consume de manera síncrona a `MS-Catálogo` para bloqueos de stock previos al checkout[cite: 1].
4.  **MS-Citas (Puerto 8083):** Gestión de agendas médicas distribuidas por sucursales y tipos de servicio oftalmológico. Valida concurrencias horarias y despacha notificaciones automáticas.

### Capa de Persistencia y Mensajería
*   **PostgreSQL (Puerto 5432):** Almacenamiento aislado e independiente por microservicio (`auth_db`, `catalogo_db`, `ventas_db`, `citas_db`) mediante contenedores aislados.
*   **RabbitMQ (Puerto 5672 / Management: 15672):** Broker encargado del procesamiento asíncrono conducido por eventos (*Event-Driven Architecture*)[cite: 1, 2]. Permite que acciones críticas (ej: `PedidoCreadoEvent` o `CitaAgendadaEvent`) actualicen el stock y notifiquen reactivamente sin bloquear los hilos principales de ejecución[cite: 1].

---

## 🛠️ Stack Tecnológico
*   **Core Backend:** Java 17, Spring Boot 3.2.x, Spring Cloud (Gateway, Config, Netflix Eureka)[cite: 1, 2].
*   **Seguridad:** Spring Security, JSON Web Tokens (JWT), BCrypt.
*   **Persistencia:** Spring Data JPA, Hibernate, PostgreSQL 15+.
*   **Mensajería:** RabbitMQ (AMQP Protocol).
*   **Productividad y Documentación:** Spring Doc (Swagger UI / OpenAPI 3.0), Lombok, ModelMapper[cite: 1, 2].
*   **Construcción e Infraestructura:** Apache Maven 3.8+, Docker & Docker Compose[cite: 1, 2].

---

## 🚀 Guía de Instalación y Despliegue

### Requisitos Previos
*   Contar con un mínimo de **16 GB de Memoria RAM** libre[cite: 2].
*   JDK 17 instalado correctamente[cite: 2].
*   Servidores locales o contenedores activos de PostgreSQL y RabbitMQ[cite: 2].

### 1. Clonar e Inicializar el Backend
Desde el directorio raíz de la solución, ejecute la compilación y empaquetado omitiendo la suite de pruebas unitarias:
```bash
mvn clean install -DskipTests
