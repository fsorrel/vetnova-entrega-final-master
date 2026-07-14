package cl.vetnova.reportes.dto;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ReporteRequest {
    @NotBlank(message = "El tipo de reporte es obligatorio")
    private String tipo;
    @NotBlank(message = "La sucursal es obligatoria")
    private String sucursal;
    @NotNull(message = "La fecha desde es obligatoria")
    private LocalDate desde;
    @NotNull(message = "La fecha hasta es obligatoria")
    private LocalDate hasta;
    @NotNull(message = "El generadoPor es obligatorio")
    private Long generadoPor;

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getSucursal() { return sucursal; }
    public void setSucursal(String sucursal) { this.sucursal = sucursal; }
    public LocalDate getDesde() { return desde; }
    public void setDesde(LocalDate desde) { this.desde = desde; }
    public LocalDate getHasta() { return hasta; }
    public void setHasta(LocalDate hasta) { this.hasta = hasta; }
    public Long getGeneradoPor() { return generadoPor; }
    public void setGeneradoPor(Long generadoPor) { this.generadoPor = generadoPor; }
}
