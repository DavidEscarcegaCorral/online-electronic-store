package objetosNegocio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Business Object para Configuración de PC.
 * Contiene la lógica de negocio para una configuración guardada.
 */
public class ConfiguracionBO implements IConfiguracionBO {
    private String id;
    private String usuarioId;
    private String nombre;
    private List<Map<String, Object>> componentes;
    private BigDecimal precioTotal;
    private LocalDateTime fechaCreacion;

    public ConfiguracionBO() {
        this.componentes = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public boolean esValida() {
        return componentes != null && !componentes.isEmpty();
    }

    public int getCantidadComponentes() {
        return componentes != null ? componentes.size() : 0;
    }
}

