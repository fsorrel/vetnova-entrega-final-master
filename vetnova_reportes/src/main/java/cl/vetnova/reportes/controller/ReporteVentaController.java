package cl.vetnova.reportes.controller;

import jakarta.validation.Valid;

import cl.vetnova.reportes.model.ReporteVenta;
import cl.vetnova.reportes.service.ReporteVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportes-venta")
public class ReporteVentaController {

    private final ReporteVentaService service;

    public ReporteVentaController(ReporteVentaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReporteVenta> crear(@Valid @RequestBody ReporteVenta request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }
}
