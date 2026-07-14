package cl.vetnova.facturacion.controller;

import jakarta.validation.Valid;

import cl.vetnova.facturacion.dto.EnvioSIIRequest;
import cl.vetnova.facturacion.dto.ProcesarRespuestaSiiRequest;
import cl.vetnova.facturacion.model.EnvioSII;
import cl.vetnova.facturacion.service.EnvioSIIService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/envios-sii")
public class EnvioSIIController {

    private final EnvioSIIService service;

    public EnvioSIIController(EnvioSIIService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EnvioSII> enviar(@Valid @RequestBody EnvioSIIRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.enviar(request));
    }

    @PutMapping("/{id}/procesar-respuesta")
    public ResponseEntity<EnvioSII> procesarRespuesta(@PathVariable Long id, @Valid @RequestBody ProcesarRespuestaSiiRequest request) {
        return ResponseEntity.ok(service.procesarRespuesta(id, request));
    }

    @PostMapping("/{id}/reintentar")
    public ResponseEntity<EnvioSII> reintentar(@PathVariable Long id) {
        return ResponseEntity.ok(service.reintentar(id));
    }
}
