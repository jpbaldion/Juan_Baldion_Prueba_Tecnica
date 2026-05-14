# Gestión de Pólizas de Seguros - API REST

## 📋 Descripción del Proyecto

Sistema de gestión integral de pólizas de seguros que proporciona una API REST para administrar pólizas individuales y colectivas, así como sus riesgos asociados. El sistema incluye funcionalidades de renovación, cancelación y validación de pólizas, además de integración con un servicio externo (CORE Mock) para notificación de cambios.

### Características Principales

- **Gestión de Pólizas**: Crear, listar, renovar y cancelar pólizas
- **Dos Tipos de Pólizas**: 
  - Pólizas Individuales (un riesgo por póliza)
  - Pólizas Colectivas (múltiples riesgos por póliza)
- **Gestión de Riesgos**: Agregar y cancelar riesgos asociados a pólizas colectivas
- **Cálculo de Renovación**: Incremento automático de prima y canon mensual basado en IPC
- **Notificación a CORE**: Integración con servicio externo para registrar cambios en operaciones

## 🛠️ Tecnologías Utilizadas

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Hibernate ORM**
- **Base de datos H2 (en memoria para desarrollo)**
- **Maven**
- **Lombok** (para reducir boilerplate)
- **ModelMapper** (para mapeo de DTOs)

## 📁 Estructura del Proyecto

```
src/main/java/com/pruebatecnica/poliza/
├── PolizaApplication.java          # Entrada de la aplicación
├── config/
│   └── ApplicationConfig.java       # Configuración de beans
├── controllers/
│   ├── DefaultController.java       # Controlador por defecto
│   ├── PolizaController.java        # Endpoints de pólizas
│   ├── RiesgoController.java        # Endpoints de riesgos
│   ├── CoreMockController.java      # Mock externo obligatorio
│   ├── CoreMockRequest.java         # DTO para peticiones al mock
├── entities/
│   ├── BaseEntity.java              # Entidad base con ID común
│   ├── PolizaEntity.java            # Entidad abstracta de póliza
│   ├── PolizaIndividualEntity.java  # Póliza individual
│   ├── PolizaColectivaEntity.java   # Póliza colectiva
│   └── RiesgoEntity.java            # Entidad de riesgo
├── enums/
│   ├── EstadoPoliza.java            # Estados: ACTIVA, RENOVADA, CANCELADA
│   └── EstadoRiesgo.java            # Estados: ACTIVO, CANCELADO
├── repositories/
│   ├── PolizaRepository.java        # JPA Repository para pólizas
│   └── RiesgoRepository.java        # JPA Repository para riesgos
└── services/
    ├── PolizaService.java           # Lógica de negocio de pólizas
    └── RiesgoService.java           # Lógica de negocio de riesgos
```

## 🚀 Requisitos Previos

- Java 17 o superior
- Maven 3.6+
- Un puerto disponible (por defecto 8080)

## 📥 Instalación y Despliegue

### 1. Clonar el Repositorio

```bash
git clone https://github.com/jpbaldion/Juan_Baldion_Prueba_Tecnica.git
cd Juan_Baldion_Prueba_Tecnica
```

### 2. Compilar el Proyecto

```bash
mvn clean install
```

### 3. Ejecutar la Aplicación

#### Opción A: Usando Maven
```bash
mvn spring-boot:run
```

#### Opción B: Ejecutar el JAR compilado
```bash
java -jar target/poliza-0.0.1-SNAPSHOT.jar
```

### 4. Acceder a la Aplicación

- **API REST**: [http://localhost:8080/api](http://localhost:8080/api)
- **Consola H2**: [http://localhost:8080/api/h2-console](http://localhost:8080/api/h2-console)
  - JDBC URL: `jdbc:h2:mem:poliza`
  - Username: `sa`
  - Password: `password`

## 📡 Endpoints Principales

### Pólizas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/polizas` | Listar todas las pólizas |
| GET | `/api/polizas?tipo=INDIVIDUAL&estado=ACTIVA` | Filtrar por tipo y estado |
| POST | `/api/polizas/{id}/renovar` | Renovar una póliza |
| POST | `/api/polizas/{id}/cancelar` | Cancelar una póliza |

### Riesgos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/polizas/{id}/riesgos` | Listar riesgos de una póliza |
| POST | `/api/polizas/{id}/riesgos` | Agregar riesgo a póliza colectiva |
| POST | `/api/riesgos/{id}/cancelar` | Cancelar un riesgo |

### Mock Externo (CORE)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/core-mock/evento` | Notificar cambios al CORE (Seguridad: header `x-api-key`) |

**Ejemplo de uso del endpoint Mock:**
```bash
curl -X POST http://localhost:8080/api/core-mock/evento \
  -H "Content-Type: application/json" \
  -H "x-api-key: 123456" \
  -d '{
    "evento": "ACTUALIZACION",
    "polizaId": "555"
  }'
```

## ⚙️ Configuración

### Archivo: `application.properties`

```properties
# Aplicación
spring.application.name=poliza
server.servlet.context-path=/api

# Base de datos H2
spring.datasource.url=jdbc:h2:mem:poliza
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true

# Hibernate
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

# Mock Externo
core-mock.url=http://localhost:8080/api/core-mock/evento
core-mock.api-key=123456
```

### Variables Personalizables

- **IPC (Índice de Precios al Consumidor)**: Por defecto `0.05` (5%)
  ```properties
  poliza.ipc=0.05
  ```

- **URL del CORE Mock**: Cambiar si se despliega en otro servidor
  ```properties
  core-mock.url=<URL_DEL_CORE>
  core-mock.api-key=<API_KEY_SEGURA>
  ```

## 🔒 Seguridad

- El endpoint `/core-mock/evento` requiere el header `x-api-key: 123456`
- Sin este header, la petición es rechazada con código 401 (Unauthorized)
- Se recomienda cambiar la clave en producción mediante variables de entorno

## 📊 Estados de Póliza

- **ACTIVA**: Póliza activa y vigente
- **RENOVADA**: Póliza renovada con primas actualizadas
- **CANCELADA**: Póliza cancelada y todos sus riesgos cancelados

## 📊 Estados de Riesgo

- **ACTIVO**: Riesgo vigente
- **CANCELADO**: Riesgo cancelado

## 🧪 Pruebas

Ejecutar pruebas unitarias:
```bash
mvn test
```

## 📝 Ejemplo de Flujo Completo

1. **Listar pólizas**:
   ```bash
   curl http://localhost:8080/api/polizas
   ```

2. **Renovar una póliza**:
   ```bash
   curl -X POST http://localhost:8080/api/polizas/1/renovar
   ```
   - Incrementa prima y canon según IPC
   - Notifica al CORE Mock
   - Cambia estado a RENOVADA

3. **Consultar riesgos**:
   ```bash
   curl http://localhost:8080/api/polizas/2/riesgos
   ```

4. **Cancelar póliza**:
   ```bash
   curl -X POST http://localhost:8080/api/polizas/3/cancelar
   ```
   - Cancela todos los riesgos asociados
   - Notifica al CORE Mock
   - Cambia estado a CANCELADA

## 🐛 Troubleshooting

### Puerto 8080 en uso
```bash
# Cambiar puerto en application.properties
server.port=8081
```

### Error de conexión a CORE Mock
- Verificar que la URL está correctamente configurada
- Revisar logs en consola (aparece warning si falla, pero continúa)
- Validar que el header `x-api-key` sea correcto

### H2 Console no accesible
- Verificar que `spring.h2.console.enabled=true` en properties
- Acceder a [http://localhost:8080/api/h2-console](http://localhost:8080/api/h2-console)

## 📧 Autor

Juan Baldión

## 📄 Licencia

Este proyecto es parte de una prueba técnica.
