package entidades;

import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Entidad que representa un producto en MongoDB.
 *
 * NOTA: Usa BigDecimal para el precio (evita problemas de precisión de Double).
 */
public class ProductoEntidad {
    private ObjectId id;
    private String nombre;
    private String categoria;
    private String marca;
    private BigDecimal precio;
    private Integer stock;
    private Map<String, String> especificaciones;
    private String descripcion;
    private String imagenUrl;

    public ProductoEntidad() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Map<String, String> getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(Map<String, String> especificaciones) {
        this.especificaciones = especificaciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}

