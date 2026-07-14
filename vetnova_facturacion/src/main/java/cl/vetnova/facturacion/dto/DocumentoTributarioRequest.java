package cl.vetnova.facturacion.dto;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;

public class DocumentoTributarioRequest {
    @NotNull(message = "El ordenId es obligatorio")
    private Long ordenId;
    private Long clienteId;
    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipo;
    private Double neto;
    private Double total;
    private String rutEmisor;
    private String rutReceptor;
    private String sucursal;

    public Long getOrdenId() { return ordenId; }
    public void setOrdenId(Long ordenId) { this.ordenId = ordenId; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Double getNeto() { return neto; }
    public void setNeto(Double neto) { this.neto = neto; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public String getRutEmisor() { return rutEmisor; }
    public void setRutEmisor(String rutEmisor) { this.rutEmisor = rutEmisor; }
    public String getRutReceptor() { return rutReceptor; }
    public void setRutReceptor(String rutReceptor) { this.rutReceptor = rutReceptor; }
    public String getSucursal() { return sucursal; }
    public void setSucursal(String sucursal) { this.sucursal = sucursal; }
}
