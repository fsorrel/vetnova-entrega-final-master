package cl.vetnova.fichaclinica.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import cl.vetnova.fichaclinica.client.AgendaClient;
import cl.vetnova.fichaclinica.exception.BusinessRuleException;
import cl.vetnova.fichaclinica.exception.ResourceNotFoundException;
import cl.vetnova.fichaclinica.model.Evolucion;
import cl.vetnova.fichaclinica.model.FichaClinica;
import cl.vetnova.fichaclinica.repository.EvolucionRepository;
import cl.vetnova.fichaclinica.repository.FichaClinicaRepository;

public class EvolucionServiceTest {

    @Mock
    private EvolucionRepository evolucionRepository;
    @Mock
    private FichaClinicaRepository fichaClinicaRepository;
    @Mock
    private AgendaClient agendaClient;
    @InjectMocks
    private EvolucionService evolucionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Map<Object, Object> citaData = new HashMap<>();
        citaData.put("mascotaId", 1L);
        doReturn(citaData).when(agendaClient).obtenerCita(any());
        FichaClinica ficha = new FichaClinica();
        ficha.setMascotaId(1L);
        when(fichaClinicaRepository.findById(1L)).thenReturn(Optional.of(ficha));
    }

    private Evolucion evolucion(Long fichaId, Long vetId, String descripcion) {
        Evolucion e = new Evolucion();
        e.setFichaId(fichaId);
        e.setVeterinarioId(vetId);
        e.setCitaId(1L);
        e.setDescripcion(descripcion);
        return e;
    }

    @Test
    void testCrearFichaIdNull() {
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> evolucionService.crear(evolucion(null, 2L, "Revisión")));
        assertEquals("El fichaId es obligatorio", ex.getMessage());
    }

    @Test
    void testCrearFichaInexistente() {
        when(fichaClinicaRepository.existsById(999L)).thenReturn(false);
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> evolucionService.crear(evolucion(999L, 2L, "Revisión")));
        assertEquals("Ficha clínica no encontrada", ex.getMessage());
    }

    @Test
    void testCrearVeterinarioNull() {
        when(fichaClinicaRepository.existsById(1L)).thenReturn(true);
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> evolucionService.crear(evolucion(1L, null, "Revisión")));
        assertEquals("El veterinarioId es obligatorio", ex.getMessage());
    }

    @Test
    void testCrearDescripcionNull() {
        when(fichaClinicaRepository.existsById(1L)).thenReturn(true);
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> evolucionService.crear(evolucion(1L, 2L, null)));
        assertEquals("La descripción es obligatoria", ex.getMessage());
    }

    @Test
    void testCrearDescripcionVacia() {
        when(fichaClinicaRepository.existsById(1L)).thenReturn(true);
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> evolucionService.crear(evolucion(1L, 2L, "   ")));
        assertEquals("La descripción no puede estar vacía", ex.getMessage());
    }

    @Test
    void testCrearCasoFeliz() {
        when(fichaClinicaRepository.existsById(1L)).thenReturn(true);
        when(evolucionRepository.save(any(Evolucion.class))).thenAnswer(inv -> inv.getArgument(0));
        Evolucion creada = evolucionService.crear(evolucion(1L, 2L, "Paciente estable"));
        assertNotNull(creada.getFechaRegistro());
    }

    @Test
    void testCrearCitaIdNull() {
        when(fichaClinicaRepository.existsById(1L)).thenReturn(true);
        Evolucion e = new Evolucion();
        e.setFichaId(1L);
        e.setVeterinarioId(2L);
        e.setCitaId(null);
        e.setDescripcion("Revisión");
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> evolucionService.crear(e));
        assertEquals("El citaId es obligatorio para registrar una evolución", ex.getMessage());
    }

    @Test
    void testCrearCitaDeOtraMascotaLanzaBusinessRule() {
        when(fichaClinicaRepository.existsById(1L)).thenReturn(true);
        Map<Object, Object> otraCita = new HashMap<>();
        otraCita.put("mascotaId", 99L);
        when(agendaClient.obtenerCita(any())).thenReturn(otraCita);
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> evolucionService.crear(evolucion(1L, 2L, "Revisión")));
        assertEquals("La cita pertenece a una mascota distinta a la de la ficha clínica", ex.getMessage());
    }

    @Test
    void testCrearCitaDataNullGuardaIgual() {
        when(fichaClinicaRepository.existsById(1L)).thenReturn(true);
        when(agendaClient.obtenerCita(any())).thenReturn(null);
        when(evolucionRepository.save(any(Evolucion.class))).thenAnswer(inv -> inv.getArgument(0));
        assertNotNull(evolucionService.crear(evolucion(1L, 2L, "Revisión")).getFechaRegistro());
    }

    @Test
    void testCrearCitaSinMascotaIdGuardaIgual() {
        when(fichaClinicaRepository.existsById(1L)).thenReturn(true);
        when(agendaClient.obtenerCita(any())).thenReturn(new HashMap<>());
        when(evolucionRepository.save(any(Evolucion.class))).thenAnswer(inv -> inv.getArgument(0));
        assertNotNull(evolucionService.crear(evolucion(1L, 2L, "Revisión")).getFechaRegistro());
    }

    @Test
    void testCrearAgendaNoDisponibleGuardaIgual() {
        when(fichaClinicaRepository.existsById(1L)).thenReturn(true);
        when(agendaClient.obtenerCita(any())).thenThrow(new RuntimeException("agenda caída"));
        when(evolucionRepository.save(any(Evolucion.class))).thenAnswer(inv -> inv.getArgument(0));
        assertNotNull(evolucionService.crear(evolucion(1L, 2L, "Revisión")).getFechaRegistro());
    }

    @Test
    void testListarPorFicha() {
        when(evolucionRepository.findByFichaIdOrderByFechaRegistroAsc(1L)).thenReturn(List.of(new Evolucion()));
        assertEquals(1, evolucionService.listarPorFicha(1L).size());
    }

    @Test
    void testListar() {
        when(evolucionRepository.findAll()).thenReturn(List.of(new Evolucion()));
        assertEquals(1, evolucionService.listar().size());
    }
}
