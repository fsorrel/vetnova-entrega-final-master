package cl.vetnova.inventario.dto;

// Snapshot de la definición del producto que vive en el MS Catálogo (fuente de verdad).
public class CatalogoProductoDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Boolean activo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
