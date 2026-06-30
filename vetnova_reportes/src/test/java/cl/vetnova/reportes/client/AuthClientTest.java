package cl.vetnova.reportes.client;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

class AuthClientTest {

    @Mock
    private RestTemplate restTemplate;

    private AuthClient authClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authClient = new AuthClient(restTemplate);
    }

    @Test
    void testUsuarioExisteTrue() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(Map.of("existe", true));
        assertTrue(authClient.usuarioExiste(1L));
    }

    @Test
    void testUsuarioExisteFalse() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(Map.of("existe", false));
        assertFalse(authClient.usuarioExiste(1L));
    }

    @Test
    void testUsuarioExisteRespuestaNull() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);
        assertFalse(authClient.usuarioExiste(1L));
    }

    @Test
    void testUsuarioExisteAuthCaidaLanzaRuntime() {
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new RuntimeException("connection refused"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> authClient.usuarioExiste(1L));
        assertTrue(ex.getMessage().contains("servicio de autenticación no disponible"));
    }
}
