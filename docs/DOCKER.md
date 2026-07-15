# Despliegue con Docker — VetNova

Orquestación local de los **12 microservicios + API Gateway** con Docker Compose.

## Requisitos
- **Docker Desktop** instalado y corriendo (incluye `docker compose`).
- ~6-8 GB de RAM libres (se levantan 13 JVMs). Cerrar apps pesadas antes.

## Cómo levantarlo

```bash
# Desde la raíz del proyecto (donde está docker-compose.yml):

# 1) Construir las imágenes (la 1ra vez tarda: compila cada servicio desde su código).
#    Hazlo ANTES de la defensa, no en vivo.
docker compose build

# 2) Levantar todo el ecosistema en segundo plano
docker compose up -d

# Ver el estado de los contenedores
docker compose ps

# Ver logs (todos, o de un servicio)
docker compose logs -f
docker compose logs -f gateway

# 3) Detener y limpiar
docker compose down
```

## Puntos de acceso

- **API Gateway (entrada única):** http://localhost:8080
- Cada microservicio queda también accesible directo en su puerto:

| Servicio | Puerto | Swagger UI |
|---|---|---|
| auth | 8081 | http://localhost:8081/swagger-ui.html |
| catalogo | 8082 | http://localhost:8082/swagger-ui.html |
| inventario | 8083 | http://localhost:8083/swagger-ui.html |
| ventas | 8084 | http://localhost:8084/swagger-ui.html |
| envio | 8085 | http://localhost:8085/swagger-ui.html |
| agenda | 8086 | http://localhost:8086/swagger-ui.html |
| ficha | 8087 | http://localhost:8087/swagger-ui.html |
| soporte | 8088 | http://localhost:8088/swagger-ui.html |
| laboratorio | 8089 | http://localhost:8089/swagger-ui.html |
| facturacion | 8090 | http://localhost:8090/swagger-ui.html |
| reportes | 8091 | http://localhost:8091/swagger-ui.html |
| notificaciones | 8092 | http://localhost:8092/swagger-ui.html |

## Cómo funciona la comunicación entre contenedores

Cada contenedor se resuelve por su **nombre de servicio** en la red interna `vetnova-net`
(ej.: `http://catalogo:8082`). Las URLs se inyectan por **variable de entorno**, sin tocar
el código:

- **Microservicios** — Spring resuelve `${app.catalogo-service-url}` desde la variable
  `APP_CATALOGO_SERVICE_URL` (binding relajado). Sin Docker, usa el default `localhost` del
  `application.yml`. Los dos modos conviven sin cambios.
- **Gateway** — cada ruta usa `uri: ${MS_CATALOGO_URI:http://localhost:8082}`. El default
  `localhost` sirve para correr sin Docker; en Docker, Compose inyecta `MS_CATALOGO_URI=http://catalogo:8082`.

## Notas

- Las imágenes compilan con `-DskipTests` (las pruebas se corren aparte con `probar-todos.ps1`).
- Las bases H2 viven dentro de cada contenedor (efímeras); `docker compose down -v` las borra.
- El Gateway usa **Java 25** (Spring Boot 4); los 12 servicios usan **Java 17**. Los Dockerfiles
  ya apuntan a la imagen base correcta en cada caso.
