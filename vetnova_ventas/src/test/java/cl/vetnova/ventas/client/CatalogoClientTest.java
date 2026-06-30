package cl.vetnova.ventas.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cl.vetnova.ventas.exception.RemoteServiceException;
import cl.vetnova.ventas.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class CatalogoClientTest {

    private CatalogoClient clientWith(ExchangeFunction exchange) {
        return new CatalogoClient(WebClient.builder().exchangeFunction(exchange), "http://localhost:8082");
    }

    @Test
    void testValidarProductoExisteOk() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE).body("{\"id\":1}").build()));
        assertDoesNotThrow(() -> clientWith(ex).validarProductoExiste(1L));
    }

    @Test
    void testValidarProductoBodyNullLanzaNotFound() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(ClientResponse.create(HttpStatus.OK).build()));
        assertThrows(ResourceNotFoundException.class, () -> clientWith(ex).validarProductoExiste(1L));
    }

    @Test
    void testValidarProducto404LanzaNotFound() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(ClientResponse.create(HttpStatus.NOT_FOUND).build()));
        assertThrows(ResourceNotFoundException.class, () -> clientWith(ex).validarProductoExiste(1L));
    }

    @Test
    void testValidarProductoCatalogoCaidoLanzaRemote() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR).build()));
        assertThrows(RemoteServiceException.class, () -> clientWith(ex).validarProductoExiste(1L));
    }
}
