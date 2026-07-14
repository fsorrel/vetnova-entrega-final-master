package cl.vetnova.inventario.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String sku;

    // Enlace explícito al producto en el MS Catálogo (fuente de verdad de la definición del producto).
    // Inventario es fuente de verdad SOLO del stock/SKU.
    @Column(name = "catalogo_producto_id")
    private Long catalogoProductoId;

    // nombre / descripcion / precio son un SNAPSHOT (caché) de Catálogo, no una copia ciega.
    // Se refrescan al crear o vía POST /{id}/sincronizar. Catálogo manda si difieren.
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockSucursal> stockSucursales = new ArrayList<>();

    public Producto() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public Long getCatalogoProductoId() { return catalogoProductoId; }
    public void setCatalogoProductoId(Long catalogoProductoId) { this.catalogoProductoId = catalogoProductoId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public List<StockSucursal> getStockSucursales() { return stockSucursales; }
    public void setStockSucursales(List<StockSucursal> stockSucursales) { this.stockSucursales = stockSucursales; }
}
