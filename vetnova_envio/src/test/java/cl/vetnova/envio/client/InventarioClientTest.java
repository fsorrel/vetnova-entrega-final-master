package cl.vetnova.envio.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cl.vetnova.envio.exception.RemoteServiceException;
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
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .build();
    }

    private static ClientResponse status(HttpStatus s) {
        return ClientResponse.create(s).build();
    }

    private InventarioClient clientWith(ExchangeFunction exchange) {
        return new InventarioClient(WebClient.builder().exchangeFunction(exchange), "http://localhost:8083");
    }

    @Test
    void testRegistrarMovimientoSalidaOk() {
        ExchangeFunction exchange = mock(ExchangeFunction.class);
        when(exchange.exchange(any()))
                .thenReturn(Mono.just(json("{\"id\":10}")), Mono.just(status(HttpStatus.OK)));
        InventarioClient client = clientWith(exchange);

        assertDoesNotThrow(() -> client.registrarMovimiento(1L, "CHILLAN", "SALIDA", 5, "Transferencia"));
    }

    @Test
    void testRegistrarMovimientoEntradaOk() {
        ExchangeFunction exchange = mock(ExchangeFunction.class);
        when(exchange.exchange(any()))
                .thenReturn(Mono.just(json("{\"id\":7}")), Mono.just(status(HttpStatus.OK)));
        InventarioClient client = clientWith(exchange);

        assertDoesNotThrow(() -> client.registrarMovimiento(1L, "TALCA", "ENTRADA", 3, "Reposición"));
    }

    @Test
    void testRegistrarMovimientoInventarioNoEncontradoBodyNull() {
        ExchangeFunction exchange = mock(ExchangeFunction.class);
        when(exchange.exchange(any())).thenReturn(Mono.just(status(HttpStatus.OK))); // sin body
        InventarioClient client = clientWith(exchange);

        assertThrows(RemoteServiceException.class,
                () -> client.registrarMovimiento(1L, "CHILLAN", "SALIDA", 5, "Transferencia"));
    }

    @Test
    void testRegistrarMovimientoInventarioSinId() {
        ExchangeFunction exchange = mock(ExchangeFunction.class);
        when(exchange.exchange(any())).thenReturn(Mono.just(json("{}")));
        InventarioClient client = clientWith(exchange);

        assertThrows(RemoteServiceException.class,
                () -> client.registrarMovimiento(1L, "CHILLAN", "SALIDA", 5, "Transferencia"));
    }

    @Test
    void testRegistrarMovimientoBuscarFalla() {
        ExchangeFunction exchange = mock(ExchangeFunction.class);
        when(exchange.exchange(any())).thenReturn(Mono.just(status(HttpStatus.INTERNAL_SERVER_ERROR)));
        InventarioClient client = clientWith(exchange);

        assertThrows(RemoteServiceException.class,
                () -> client.registrarMovimiento(1L, "CHILLAN", "SALIDA", 5, "Transferencia"));
    }

    @Test
    void testRegistrarMovimientoPostFalla() {
        ExchangeFunction exchange = mock(ExchangeFunction.class);
        when(exchange.exchange(any()))
                .thenReturn(Mono.just(json("{\"id\":10}")), Mono.just(status(HttpStatus.INTERNAL_SERVER_ERROR)));
        InventarioClient client = clientWith(exchange);

        assertThrows(RemoteServiceException.class,
                () -> client.registrarMovimiento(1L, "CHILLAN", "SALIDA", 5, "Transferencia"));
    }
}
