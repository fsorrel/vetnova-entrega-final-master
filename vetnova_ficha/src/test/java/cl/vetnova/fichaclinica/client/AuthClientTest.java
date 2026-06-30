package cl.vetnova.fichaclinica.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class AuthClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthClient authClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testObtenerNombreClienteCompleto() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(Map.of("nombre", "Ana", "apellido", "Soto")));
        assertEquals("Ana Soto", authClient.obtenerNombreCliente(1L));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testObtenerNombreClienteSinCampos() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(Map.of()));
        assertEquals("", authClient.obtenerNombreCliente(1L));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testObtenerNombreClienteBodyNull() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(null));
        assertNull(authClient.obtenerNombreCliente(1L));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testObtenerNombreClienteExcepcionRetornaNull() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("auth caído"));
        assertNull(authClient.obtenerNombreCliente(1L));
    }

    @Test
    void testUsuarioExisteTrue() {
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(Boolean.TRUE);
        assertTrue(authClient.usuarioExiste(1L));
    }

    @Test
    void testUsuarioExisteFalse() {
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(Boolean.FALSE);
        assertFalse(authClient.usuarioExiste(1L));
    }

    @Test
    void testUsuarioExisteExcepcionRetornaFalse() {
        when(restTemplate.getForObject(anyString(), eq(Boolean.class)))
                .thenThrow(new RuntimeException("auth caído"));
        assertFalse(authClient.usuarioExiste(1L));
    }
}
