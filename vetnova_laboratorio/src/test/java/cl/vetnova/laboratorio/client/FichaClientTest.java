package cl.vetnova.laboratorio.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cl.vetnova.laboratorio.exception.RemoteServiceException;
import cl.vetnova.laboratorio.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class FichaClientTest {

    private static ClientResponse json(String body) {
        return ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE).body(body).build();
    }

    private static ClientResponse status(HttpStatus s) {
        return ClientResponse.create(s).build();
    }

    private FichaClient clientWith(ExchangeFunction exchange) {
        return new FichaClient(WebClient.builder().exchangeFunction(exchange), "http://localhost:8087");
    }

    // ---- obtenerNombreMascota ----

    @Test
    void testObtenerNombreMascotaOk() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(json("{\"nombre\":\"Firulais\"}")));
        assertEquals("Firulais", clientWith(ex).obtenerNombreMascota(1L));
    }

    @Test
    void testObtenerNombreMascotaBodyNull() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(status(HttpStatus.OK)));
        assertNull(clientWith(ex).obtenerNombreMascota(1L));
    }

    @Test
    void testObtenerNombreMascotaErrorRetornaNull() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(status(HttpStatus.INTERNAL_SERVER_ERROR)));
        assertNull(clientWith(ex).obtenerNombreMascota(1L));
    }

    // ---- validarMascotaActiva ----

    @Test
    void testValidarMascotaActivaOk() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(json("{\"activo\":true}")));
        assertDoesNotThrow(() -> clientWith(ex).validarMascotaActiva(1L));
    }

    @Test
    void testValidarMascotaBodyNullLanzaNotFound() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(status(HttpStatus.OK)));
        assertThrows(ResourceNotFoundException.class, () -> clientWith(ex).validarMascotaActiva(1L));
    }

    @Test
    void testValidarMascotaDesactivadaLanzaNotFound() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(json("{\"activo\":false}")));
        assertThrows(ResourceNotFoundException.class, () -> clientWith(ex).validarMascotaActiva(1L));
    }

    @Test
    void testValidarMascota404LanzaNotFound() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(status(HttpStatus.NOT_FOUND)));
        assertThrows(ResourceNotFoundException.class, () -> clientWith(ex).validarMascotaActiva(1L));
    }

    @Test
    void testValidarMascotaFichaCaidaLanzaRemote() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(status(HttpStatus.INTERNAL_SERVER_ERROR)));
        assertThrows(RemoteServiceException.class, () -> clientWith(ex).validarMascotaActiva(1L));
    }
}
