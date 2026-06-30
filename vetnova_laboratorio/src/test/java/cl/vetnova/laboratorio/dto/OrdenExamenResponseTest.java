package cl.vetnova.laboratorio.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import cl.vetnova.laboratorio.model.OrdenExamen;
import cl.vetnova.laboratorio.model.TipoExamen;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class OrdenExamenResponseTest {

    @Test
    void testMapeaDesdeOrdenExamen() {
        TipoExamen tipo = new TipoExamen();
        OrdenExamen o = new OrdenExamen();
        o.setMascotaId(5L);
        o.setVeterinarioId(3L);
        o.setTipoExamen(tipo);
        o.setDescripcion("Hemograma");
        o.setEstado("SOLICITADO");
        LocalDateTime sol = LocalDateTime.now();
        LocalDateTime prog = sol.plusDays(1);
        o.setFechaSolicitud(sol);
        o.setFechaProgramada(prog);
        o.setMotivoCancelacion("ninguno");

        OrdenExamenResponse r = new OrdenExamenResponse(o, "Firulais");

        assertNull(r.getId());
        assertEquals(5L, r.getMascotaId());
        assertEquals("Firulais", r.getNombreMascota());
        assertEquals(3L, r.getVeterinarioId());
        assertSame(tipo, r.getTipoExamen());
        assertEquals("Hemograma", r.getDescripcion());
        assertEquals("SOLICITADO", r.getEstado());
        assertEquals(sol, r.getFechaSolicitud());
        assertEquals(prog, r.getFechaProgramada());
        assertEquals("ninguno", r.getMotivoCancelacion());
    }
}
