package entidades;

import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Entidad que representa una configuración guardada en MongoDB.
 *
 * NOTA: Usa BigDecimal para precioTotal (evita problemas de precisión de Double).
 * NOTA: Usa LocalDateTime para fechaCreacion (API moderna de Java 8+).
 */
public class ConfiguracionEntidad {
    private ObjectId id;
    private String usuarioId;
    private String nombre;
    private List<Map<String, Object>> componentes;
    private BigDecimal precioTotal;
    private LocalDateTime fechaCreacion;

    public ConfiguracionEntidad() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Map<String, Object>> getComponentes() {
        return componentes;
    }

    public void setComponentes(List<Map<String, Object>> componentes) {
        this.componentes = componentes;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}

