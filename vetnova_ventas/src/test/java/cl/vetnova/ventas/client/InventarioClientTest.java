package cl.vetnova.ventas.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cl.vetnova.ventas.exception.RemoteServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class InventarioClientTest {

    private static ClientResponse json(String body) {
        return ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE).body(body).build();
    }

    private static ClientResponse status(HttpStatus s) {
        return ClientResponse.create(s).build();
    }

    private InventarioClient clientWith(ExchangeFunction exchange) {
        return new InventarioClient(WebClient.builder().exchangeFunction(exchange), "http://localhost:8083");
    }

    @Test
    void testConsultarStockOk() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(json("{\"stockDisponible\":12}")));
        assertEquals(12, clientWith(ex).consultarStock(1L, "CHILLAN"));
    }

    @Test
    void testConsultarStockBodyNullDevuelveCero() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(status(HttpStatus.OK)));
        assertEquals(0, clientWith(ex).consultarStock(1L, "CHILLAN"));
    }

    @Test
    void testConsultarStockErrorLanzaRemote() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(status(HttpStatus.INTERNAL_SERVER_ERROR)));
        assertThrows(RemoteServiceException.class, () -> clientWith(ex).consultarStock(1L, "CHILLAN"));
    }

    @Test
    void testRegistrarSalidaOk() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(json("{\"id\":10}")), Mono.just(status(HttpStatus.OK)));
        assertDoesNotThrow(() -> clientWith(ex).registrarSalida(1L, "CHILLAN", 5, "venta"));
    }

    @Test
    void testRegistrarSalidaInventarioNoEncontrado() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(json("{}")));
        assertThrows(RemoteServiceException.class, () -> clientWith(ex).registrarSalida(1L, "CHILLAN", 5, "venta"));
    }

    @Test
    void testRegistrarSalidaBuscarFalla() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(status(HttpStatus.INTERNAL_SERVER_ERROR)));
        assertThrows(RemoteServiceException.class, () -> clientWith(ex).registrarSalida(1L, "CHILLAN", 5, "venta"));
    }

    @Test
    void testRegistrarSalidaPostFalla() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(json("{\"id\":10}")), Mono.just(status(HttpStatus.INTERNAL_SERVER_ERROR)));
        assertThrows(RemoteServiceException.class, () -> clientWith(ex).registrarSalida(1L, "CHILLAN", 5, "venta"));
    }

    @Test
    void testRegistrarEntradaOk() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(json("{\"id\":10}")), Mono.just(status(HttpStatus.OK)));
        assertDoesNotThrow(() -> clientWith(ex).registrarEntrada(1L, "CHILLAN", 5, "bodega"));
    }

    @Test
    void testRegistrarEntradaPostFalla() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(json("{\"id\":10}")), Mono.just(status(HttpStatus.INTERNAL_SERVER_ERROR)));
        assertThrows(RemoteServiceException.class, () -> clientWith(ex).registrarEntrada(1L, "CHILLAN", 5, "bodega"));
    }

    @Test
    void testRegistrarEntradaInventarioNoEncontrado() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(json("{}")));
        assertThrows(RemoteServiceException.class, () -> clientWith(ex).registrarEntrada(1L, "CHILLAN", 5, "bodega"));
    }

    @Test
    void testRegistrarSalidaBuscarRespuestaNulaLanzaRemote() {
        // El GET de búsqueda responde 200 sin cuerpo -> response == null (rama izquierda del OR).
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(status(HttpStatus.OK)));
        assertThrows(RemoteServiceException.class, () -> clientWith(ex).registrarSalida(1L, "CHILLAN", 5, "venta"));
    }
}
