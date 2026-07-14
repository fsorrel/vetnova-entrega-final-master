package cl.vetnova.inventario.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import cl.vetnova.inventario.dto.CatalogoProductoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class CatalogoClientTest {

    private WebClient webClient;

    private WebClient.Builder builderSimulado() {
        WebClient.Builder builder = mock(WebClient.Builder.class);
        webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(webClient);
        return builder;
    }

    private void simularGet(Mono<CatalogoProductoDTO> respuesta) {
        when(webClient.get().uri(anyString(), any(Object[].class))
                .retrieve().bodyToMono(CatalogoProductoDTO.class))
                .thenReturn(respuesta);
    }

    @Test
    void testObtenerProductoDevuelveLaDefinicionDeCatalogo() {
        CatalogoClient client = new CatalogoClient(builderSimulado(), "http://localhost:8082");
        CatalogoProductoDTO esperado = new CatalogoProductoDTO();
        esperado.setId(7L);
        esperado.setNombre("Alimento premium");
        simularGet(Mono.just(esperado));

        CatalogoProductoDTO resultado = client.obtenerProducto(7L);

        assertNotNull(resultado);
        assertEquals("Alimento premium", resultado.getNombre());
    }

    @Test
    void testObtenerProductoConCatalogoCaidoDevuelveNull() {
        CatalogoClient client = new CatalogoClient(builderSimulado(), "http://localhost:8082");
        simularGet(Mono.error(new RuntimeException("conexion rechazada")));

        assertNull(client.obtenerProducto(7L));
    }
}
