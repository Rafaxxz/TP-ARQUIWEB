# 🌱 EcoMinds - Plataforma de Reciclaje

Plataforma web para gestionar el reciclaje, eventos, puntos de acopio y recompensas.

## 🚀 Tecnologías

- **Backend**: Spring Boot 3.5.5
- **Base de Datos**: PostgreSQL
- **Seguridad**: Spring Security + JWT
- **Documentación API**: Swagger/OpenAPI
- **Build Tool**: Maven

## 📋 Requisitos

- Java 17
- PostgreSQL 12+
- Maven 3.6+

## 🛠️ Instalación Local

1. Clonar el repositorio:
```bash
git clone https://github.com/Rafaxxz/TP-ARQUIWEB.git
cd TP-ARQUIWEB
```

2. Configurar la base de datos en `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/db_EcoMind
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

3. Ejecutar la aplicación:
```bash
./mvnw spring-boot:run
```

4. La aplicación estará disponible en: `http://localhost:8080`

## 📚 Documentación API

Una vez iniciada la aplicación, accede a Swagger UI:
- URL: `http://localhost:8080/swagger-ui/index.html`

## 🌐 Despliegue en Render

Para desplegar esta aplicación en Render.com, tienes dos opciones:

1. **Guía Rápida**: 🚀 [RENDER_QUICK_START.md](RENDER_QUICK_START.md) - Pasos esenciales en 5 minutos
2. **Guía Completa**: 📖 [RENDER_DEPLOYMENT_GUIDE.md](RENDER_DEPLOYMENT_GUIDE.md) - Guía detallada paso a paso

Las guías incluyen:
- Configuración de base de datos PostgreSQL
- Variables de entorno necesarias
- Pasos de despliegue
- Solución de problemas comunes
- Cambios necesarios en application.properties

## 📖 Documentación Adicional

- [CODIGO_COMPLETO_19_US.md](CODIGO_COMPLETO_19_US.md) - Código completo de las User Stories
- [ESTADO_USER_STORIES.md](ESTADO_USER_STORIES.md) - Estado de las User Stories
- [GUIA_PRUEBAS_POSTMAN.md](GUIA_PRUEBAS_POSTMAN.md) - Guía de pruebas con Postman
- [IMPLEMENTACION_19_US.md](IMPLEMENTACION_19_US.md) - Implementación de las 19 User Stories

## 🔐 Seguridad

La aplicación utiliza JWT para autenticación. Para generar un hash de contraseña, usa:

```bash
java GenerateHash.java
```

## 👥 Autores

Proyecto desarrollado para el curso de Arquitectura Web - UPC

## 📝 Licencia

Este proyecto es parte de un trabajo académico.
