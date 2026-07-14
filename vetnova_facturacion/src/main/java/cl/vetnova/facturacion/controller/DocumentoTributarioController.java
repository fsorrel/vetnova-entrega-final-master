package cl.vetnova.facturacion.controller;

import jakarta.validation.Valid;

import cl.vetnova.facturacion.dto.AnularDocumentoRequest;
import cl.vetnova.facturacion.dto.DocumentoTributarioRequest;
import cl.vetnova.facturacion.model.DocumentoTributario;
import cl.vetnova.facturacion.service.DocumentoTributarioService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documentos")
public class DocumentoTributarioController {

    private final DocumentoTributarioService service;

    public DocumentoTributarioController(DocumentoTributarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DocumentoTributario>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoTributario> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DocumentoTributario> emitir(@Valid @RequestBody DocumentoTributarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.emitir(request));
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<DocumentoTributario> anular(@PathVariable Long id, @Valid @RequestBody AnularDocumentoRequest request) {
        return ResponseEntity.ok(service.anular(id, request.getMotivo()));
    }

    @PostMapping("/{id}/enviar-sii")
    public ResponseEntity<DocumentoTributario> enviarAlSII(@PathVariable Long id) {
        return ResponseEntity.ok(service.enviarAlSII(id));
    }
}
