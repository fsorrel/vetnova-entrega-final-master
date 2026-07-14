package cl.vetnova.notificaciones.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class RenderizarRequest {
    @NotNull(message = "Los valores para renderizar son obligatorios")
    private Map<String, String> valores;

    public Map<String, String> getValores() { return valores; }
    public void setValores(Map<String, String> valores) { this.valores = valores; }
}
