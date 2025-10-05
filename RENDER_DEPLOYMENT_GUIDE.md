# Guía de Despliegue en Render

Esta guía te ayudará a desplegar tu aplicación EcoMinds (Spring Boot) en Render.com.

## Requisitos Previos

1. Una cuenta en [Render.com](https://render.com)
2. Tu código en un repositorio de GitHub
3. Una base de datos PostgreSQL (puedes crear una en Render)

## Paso 1: Configurar la Base de Datos PostgreSQL en Render

1. Inicia sesión en [Render.com](https://render.com)
2. Click en **"New +"** y selecciona **"PostgreSQL"**
3. Configura tu base de datos:
   - **Name**: `ecominds-db` (o el nombre que prefieras)
   - **Database**: `db_EcoMind`
   - **User**: Se generará automáticamente
   - **Region**: Selecciona la región más cercana a tus usuarios
   - **Plan**: Free (para pruebas) o un plan pagado
4. Click en **"Create Database"**
5. **Guarda las credenciales** que se muestran:
   - Internal Database URL
   - External Database URL
   - PSQL Command
   - Username
   - Password

## Paso 2: Preparar tu Aplicación

### 2.1 Verificar system.properties

Asegúrate de que existe el archivo `system.properties` en la raíz del proyecto con:

```properties
java.runtime.version=17
```

### 2.2 Configurar application.properties

Tu archivo `application.properties` debe estar configurado para usar variables de entorno:

```properties
spring.application.name=EcoMinds

# Database Configuration (usando variables de entorno)
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/db_EcoMind}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:1234}

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# No envía el trace al usuario cuando hay error
server.error.include-stacktrace=never

# JWT Configuration
jwt.secret=${JWT_SECRET:EcoMindsSecretKey2025SuperSecureKeyForJWTTokenGenerationAndValidation}
jwt.expiration=${JWT_EXPIRATION:86400000}

# Server Configuration
server.port=${PORT:8080}
```

### 2.3 Hacer commit de los cambios

```bash
git add .
git commit -m "Configuración para despliegue en Render"
git push origin main
```

## Paso 3: Crear el Web Service en Render

1. En el dashboard de Render, click en **"New +"** y selecciona **"Web Service"**
2. Conecta tu repositorio de GitHub:
   - Si es la primera vez, autoriza a Render para acceder a tu GitHub
   - Selecciona el repositorio `TP-ARQUIWEB`
3. Configura el servicio:
   - **Name**: `ecominds-api` (o el nombre que prefieras)
   - **Region**: La misma que tu base de datos
   - **Branch**: `main` (o la rama que quieras desplegar)
   - **Runtime**: `Java`
   - **Build Command**: `./mvnw clean install -DskipTests`
   - **Start Command**: `java -jar target/TF_ArquiWeb-0.0.1-SNAPSHOT.jar`
   - **Plan**: Free (para pruebas) o un plan pagado

## Paso 4: Configurar Variables de Entorno

En la sección **"Environment"** de tu Web Service en Render, agrega las siguientes variables:

1. **DATABASE_URL**
   - Copia la "Internal Database URL" de tu base de datos PostgreSQL
   - Ejemplo: `jdbc:postgresql://dpg-xxxxx-a:5432/db_EcoMind`

2. **DB_USERNAME**
   - El usuario de tu base de datos (lo obtuviste en el Paso 1)

3. **DB_PASSWORD**
   - La contraseña de tu base de datos (la obtuviste en el Paso 1)

4. **JWT_SECRET**
   - Un valor secreto para firmar los tokens JWT
   - Genera uno seguro: `openssl rand -base64 64`
   - O usa un generador online seguro

5. **JWT_EXPIRATION**
   - Tiempo de expiración en milisegundos (ejemplo: `86400000` = 24 horas)

6. **PORT** (Render lo configura automáticamente, pero puedes verificar)
   - Valor: `8080`

### Formato de DATABASE_URL

⚠️ **IMPORTANTE**: Render proporciona una URL que comienza con `postgresql://`. Para Spring Boot, necesitas cambiarla a `jdbc:postgresql://`.

Ejemplo:
- URL de Render: `postgresql://user:pass@host:5432/dbname`
- URL para Spring Boot: `jdbc:postgresql://host:5432/dbname`

## Paso 5: Desplegar

1. Una vez configuradas todas las variables de entorno, click en **"Create Web Service"**
2. Render comenzará a:
   - Clonar tu repositorio
   - Ejecutar el build command
   - Iniciar tu aplicación
3. Monitorea los logs para ver si hay errores
4. Cuando termine, verás el estado "Live" con una URL como: `https://ecominds-api.onrender.com`

## Paso 6: Probar tu API

1. Accede a tu API usando la URL proporcionada por Render
2. Prueba el endpoint de Swagger UI: `https://tu-app.onrender.com/swagger-ui/index.html`
3. Prueba tus endpoints con Postman o curl:

```bash
# Ejemplo: Health check
curl https://tu-app.onrender.com/actuator/health

# Ejemplo: Obtener usuarios
curl https://tu-app.onrender.com/api/usuarios
```

## Paso 7: Configurar el Hook de Despliegue Automático (Opcional)

Por defecto, Render redesplegará tu aplicación cada vez que hagas push a la rama principal.

Si quieres desactivar esto:
1. Ve a **Settings** > **Build & Deploy**
2. Desactiva **"Auto-Deploy"**

## Solución de Problemas Comunes

### Error: "Failed to connect to database"

- Verifica que DATABASE_URL esté en el formato correcto (`jdbc:postgresql://...`)
- Asegúrate de que el usuario y contraseña sean correctos
- Verifica que la base de datos esté en la misma región que tu web service

### Error: "Application failed to start on port"

- Asegúrate de que `server.port=${PORT:8080}` esté en application.properties
- Render asigna dinámicamente un puerto, tu app debe escuchar en `${PORT}`

### Error: "Build failed"

- Revisa los logs de build en Render
- Asegúrate de que `./mvnw` tenga permisos de ejecución:
  ```bash
  git update-index --chmod=+x mvnw
  git commit -m "Make mvnw executable"
  git push
  ```

### La aplicación se "duerme" después de inactividad

- En el plan Free, Render pone tu app en "sleep" después de 15 minutos de inactividad
- La primera petición después de esto tomará ~30 segundos
- Para evitarlo, actualiza a un plan pagado o usa un servicio de "keep-alive"

### Error con JWT

- Asegúrate de que JWT_SECRET esté configurado en las variables de entorno
- Genera un secret seguro de al menos 32 caracteres

## Paso 8: Inicializar Datos (Opcional)

Si tienes un archivo `datos_iniciales.sql`, puedes ejecutarlo:

1. Conéctate a tu base de datos desde Render Dashboard
2. Ve a la pestaña **"Connect"** en tu PostgreSQL database
3. Usa el comando PSQL proporcionado o la web shell
4. Copia y pega el contenido de `datos_iniciales.sql`

O desde tu máquina local:

```bash
# Usa la External Database URL
psql postgresql://user:pass@host:5432/dbname < src/main/resources/datos_iniciales.sql
```

## Comandos Útiles

### Ver logs en tiempo real
En el dashboard de Render > Tu Web Service > Logs

### Reiniciar el servicio
En el dashboard de Render > Tu Web Service > Manual Deploy > "Clear build cache & deploy"

### Conectarse a la base de datos
```bash
# Usa el comando PSQL de Render
psql postgresql://user:pass@host:5432/dbname
```

## Recursos Adicionales

- [Documentación oficial de Render para Spring Boot](https://render.com/docs/deploy-spring-boot)
- [Render Disk Storage](https://render.com/docs/disks) - Si necesitas almacenamiento persistente
- [Render Environment Groups](https://render.com/docs/environment-groups) - Para compartir variables entre servicios

## Checklist de Despliegue

- [ ] Base de datos PostgreSQL creada en Render
- [ ] Variables de entorno configuradas
- [ ] `system.properties` existe con Java 17
- [ ] `application.properties` usa variables de entorno
- [ ] Permisos de ejecución en `mvnw`
- [ ] Código subido a GitHub
- [ ] Web Service creado en Render
- [ ] Aplicación desplegada exitosamente
- [ ] Endpoints probados
- [ ] Datos iniciales cargados (si aplica)

## Precios (Aproximados)

- **Plan Free**: 
  - 750 horas/mes de compute
  - La app se "duerme" después de 15 min de inactividad
  - PostgreSQL: 90 días gratis, luego $7/mes

- **Plan Starter ($7/mes)**:
  - Siempre activa (no se duerme)
  - 1 GB RAM

Para más información sobre precios: https://render.com/pricing

---

¡Tu aplicación EcoMinds ahora está desplegada en Render! 🚀

Si tienes problemas, revisa los logs en el dashboard de Render o consulta la [documentación oficial](https://render.com/docs).
