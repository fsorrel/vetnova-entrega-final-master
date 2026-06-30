package cl.vetnova.ventas.client;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

class AuthClientTest {

    private AuthClient clientWith(ExchangeFunction exchange) {
        return new AuthClient(WebClient.builder().exchangeFunction(exchange), "http://localhost:8081");
    }

    private static ClientResponse json(String body) {
        return ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE).body(body).build();
    }

    @Test
    void testClienteExisteTrue() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(json("{\"existe\":true}")));
        assertTrue(clientWith(ex).clienteExiste(1L));
    }

    @Test
    void testClienteExisteFalse() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(json("{\"existe\":false}")));
        assertFalse(clientWith(ex).clienteExiste(1L));
    }

    @Test
    void testClienteExisteBodyNull() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(ClientResponse.create(HttpStatus.OK).build()));
        assertFalse(clientWith(ex).clienteExiste(1L));
    }

    @Test
    void testClienteExisteAuthCaidaLanzaRemote() {
        ExchangeFunction ex = mock(ExchangeFunction.class);
        when(ex.exchange(any())).thenReturn(Mono.just(ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR).build()));
        assertThrows(RemoteServiceException.class, () -> clientWith(ex).clienteExiste(1L));
    }
}
