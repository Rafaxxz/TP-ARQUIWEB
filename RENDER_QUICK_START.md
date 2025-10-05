# 🚀 Render - Guía Rápida de Despliegue

## Resumen Rápido

Para desplegar tu aplicación EcoMinds en Render, necesitas:

### 1. Archivos Necesarios ✅

- ✅ `system.properties` - Ya está creado
- ⚠️ Actualizar `src/main/resources/application.properties` - Ver abajo
- ✅ `.gitignore` - Ya está creado

### 2. Cambios Requeridos en application.properties

**IMPORTANTE**: Debes actualizar tu `application.properties` en la rama `master` para usar variables de entorno.

Cambia estas líneas:

```properties
# ANTES (valores fijos)
spring.datasource.url=jdbc:postgresql://localhost:5432/db_EcoMind
spring.datasource.username=postgres
spring.datasource.password=1234
jwt.secret=EcoMindsSecretKey2025SuperSecureKeyForJWTTokenGenerationAndValidation
jwt.expiration=86400000
```

Por estas (con variables de entorno):

```properties
# DESPUÉS (usando variables de entorno con valores por defecto)
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/db_EcoMind}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:1234}
jwt.secret=${JWT_SECRET:EcoMindsSecretKey2025SuperSecureKeyForJWTTokenGenerationAndValidation}
jwt.expiration=${JWT_EXPIRATION:86400000}
server.port=${PORT:8080}
```

**También cambia**:
```properties
spring.jpa.show-sql=true   →   spring.jpa.show-sql=false
```

### 3. Hacer Ejecutable el mvnw

```bash
chmod +x mvnw
git add mvnw
git commit -m "Make mvnw executable"
git push
```

### 4. Pasos en Render

**OPCIÓN RÁPIDA**: Usa el archivo `render.yaml` incluido → New + → Blueprint → Conecta repo

**OPCIÓN MANUAL**:

1. **Crear PostgreSQL Database**
   - New + → PostgreSQL
   - Guarda las credenciales (URL, username, password)

2. **Crear Web Service**
   - New + → Web Service
   - Conecta tu repositorio GitHub
   - Build Command: `./mvnw clean install -DskipTests`
   - Start Command: `java -jar target/TF_ArquiWeb-0.0.1-SNAPSHOT.jar`

3. **Configurar Variables de Entorno**
   - `DATABASE_URL`: jdbc:postgresql://host:5432/db_EcoMind (de tu DB)
   - `DB_USERNAME`: el usuario de tu DB
   - `DB_PASSWORD`: la contraseña de tu DB
   - `JWT_SECRET`: genera uno seguro con `openssl rand -base64 64`
   - `JWT_EXPIRATION`: `86400000` (24 horas)

4. **Deploy**
   - Click "Create Web Service"
   - Espera a que se complete el build
   - Tu API estará en: `https://tu-app.onrender.com`

### 5. Verificar

Prueba tu API:
```bash
curl https://tu-app.onrender.com/swagger-ui/index.html
```

---

## 📖 Guía Completa

Para instrucciones detalladas, solución de problemas y más información:

👉 **[RENDER_DEPLOYMENT_GUIDE.md](RENDER_DEPLOYMENT_GUIDE.md)**

---

## ⚠️ Notas Importantes

1. **Plan Free**: La app se "duerme" después de 15 minutos de inactividad
2. **Primera carga**: Puede tomar ~30 segundos en despertar
3. **DATABASE_URL**: Debe empezar con `jdbc:postgresql://` (no solo `postgresql://`)
4. **Logs**: Revisa los logs en Render si algo falla

## 🆘 Problemas Comunes

| Error | Solución |
|-------|----------|
| Build failed | Verifica que `mvnw` sea ejecutable |
| Cannot connect to database | Verifica el formato de DATABASE_URL |
| Port binding error | Asegúrate de tener `server.port=${PORT:8080}` |
| JWT error | Configura JWT_SECRET en variables de entorno |

---

**¿Primer despliegue?** Sigue la guía completa paso a paso.
**¿Ya conoces Render?** Esta guía rápida es suficiente.
