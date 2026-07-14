package cl.vetnova.inventario.client;

import cl.vetnova.inventario.dto.CatalogoProductoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CatalogoClient {
    private static final Logger log = LoggerFactory.getLogger(CatalogoClient.class);
    private final WebClient webClient;

    public CatalogoClient(WebClient.Builder builder,
                          @Value("${app.catalogo-service-url}") String catalogoUrl) {
        this.webClient = builder.baseUrl(catalogoUrl).build();
    }

    // Catálogo es la fuente de verdad. Si no responde, devolvemos null y el llamador
    // aplica degradación suave (usa los datos que ya trae el request/snapshot).
    public CatalogoProductoDTO obtenerProducto(Long catalogoProductoId) {
        try {
            CatalogoProductoDTO dto = webClient.get()
                    .uri("/api/v1/productos/{id}", catalogoProductoId)
                    .retrieve()
                    .bodyToMono(CatalogoProductoDTO.class)
                    .block();
            log.info("event=catalogo_producto_obtenido catalogoProductoId={}", catalogoProductoId);
            return dto;
        } catch (Exception ex) {
            log.warn("event=catalogo_producto_no_disponible catalogoProductoId={} detalle={}",
                    catalogoProductoId, ex.getMessage());
            return null;
        }
    }
}
