# 🚲 BikeShop API: backend de gestión para tienda de bicicletas

API REST para administrar una tienda de bicicletas: inventario, proveedores, compras, clientes, ventas y reportes. Tiene autenticación con JWT y permisos según el rol del usuario.

> Frontend en Angular: [Rich4rdx/BikeShop](https://github.com/Rich4rdx/BikeShop)

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.5-6DB33F?style=flat&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security_+_JWT-6DB33F?style=flat&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL_8-4479A1?style=flat&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger&logoColor=black)

## ✨ Funcionalidades

- **Autenticación JWT** con inicio de sesión y registro de usuarios
- **Roles:** `ADMIN` con acceso total y `TRABAJADOR` con acceso a ventas y clientes
- **Inventario de bicicletas** por tipo y por proveedor, con registro de movimientos de entrada y salida
- **Proveedores y compras:** historial de compras por proveedor y por bicicleta
- **Ventas** con detalle de productos, historial por cliente y por vendedor
- **Reportes de ventas** descargables en **PDF** y **Excel**
- **Documentación interactiva** de la API con Swagger UI
- Manejo global de errores y uso de DTOs y mappers para no exponer las entidades

## 🏗️ Arquitectura

```
controller/   → Endpoints REST
service/      → Lógica de negocio, JWT y generación de reportes
repository/   → Acceso a datos con Spring Data JPA
entity/       → Entidades JPA (Bicicleta, Venta, Cliente, Proveedor…)
dto/ mapper/  → Objetos de entrada/salida y su conversión
config/       → Seguridad, filtro JWT, Swagger, manejo de errores y datos iniciales
```

## 🔌 Endpoints principales

| Recurso | Ruta base | Operaciones |
|---|---|---|
| Autenticación | `/api/auth` | `POST /login`, `POST /registro` |
| Bicicletas | `/api/bicicletas` | Crear, listar, buscar por id y por proveedor |
| Proveedores | `/api/proveedores` | CRUD por NIT y registro de compras |
| Clientes | `/api/clientes` | Crear, listar, buscar por documento |
| Ventas | `/api/ventas` | Registrar, listar, buscar por id y por cliente |
| Movimientos | `/api/movimientos` | Entradas y salidas de inventario |
| Usuarios | `/api/usuarios` | Listar, crear y ver ventas por usuario |
| Reportes | `/api/reportes` | `GET /ventas`, `/ventas/pdf`, `/ventas/excel` |

Con la aplicación corriendo, la documentación completa está en **http://localhost:8080/swagger-ui.html**.

## 🚀 Cómo ejecutarlo

### Opción 1: con Docker (recomendada)

```bash
cp .env.example .env      # y edita los valores
docker compose up --build
```

Esto levanta MySQL y la API. La API queda en `http://localhost:8080` y MySQL en el puerto `3307`.

### Opción 2: local

Requisitos: Java 21 y MySQL 8 corriendo en `localhost:3306` con una base de datos `tienda_bicicletas`.

```bash
# Define las variables de entorno (ver .env.example) y luego:
./mvnw spring-boot:run
```

## 🔐 Variables de entorno

| Variable | Descripción |
|---|---|
| `DB_URL` | URL JDBC de MySQL (por defecto `localhost:3306/tienda_bicicletas`) |
| `DB_USERNAME` / `DB_PASSWORD` | Credenciales de la base de datos |
| `JWT_SECRET` | Clave para firmar los tokens, mínimo 32 caracteres |
| `ADMIN_PASSWORD` | Contraseña del usuario inicial `admin` |
| `TRABAJADOR_PASSWORD` | Contraseña del usuario inicial `trabajador` |

Si `JWT_SECRET` no está definido, la aplicación genera una clave temporal al arrancar. Si faltan las contraseñas de los usuarios iniciales, los crea con una contraseña aleatoria que aparece en el log.

## 👤 Autor

**Richard**: [github.com/Rich4rdx](https://github.com/Rich4rdx)
