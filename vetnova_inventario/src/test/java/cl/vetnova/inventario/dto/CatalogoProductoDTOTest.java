package cl.vetnova.inventario.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CatalogoProductoDTOTest {

    @Test
    void testGettersYSetters() {
        CatalogoProductoDTO dto = new CatalogoProductoDTO();
        dto.setId(5L);
        dto.setNombre("Vacuna");
        dto.setDescripcion("Antirrábica");
        dto.setPrecio(12990.0);
        dto.setActivo(true);

        assertEquals(5L, dto.getId());
        assertEquals("Vacuna", dto.getNombre());
        assertEquals("Antirrábica", dto.getDescripcion());
        assertEquals(12990.0, dto.getPrecio());
        assertTrue(dto.getActivo());
    }
}
