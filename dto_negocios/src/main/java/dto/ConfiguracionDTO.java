package dto;

import java.util.List;
import java.util.Map;

/**
 * DTO para representar una configuración de PC guardada.
 */
public class ConfiguracionDTO {
    private String id;
    private String nombre;
    private String usuarioId;
    private Double precioTotal;
    private List<Map<String, Object>> componentes;

    public ConfiguracionDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public List<Map<String, Object>> getComponentes() {
        return componentes;
    }

    public void setComponentes(List<Map<String, Object>> componentes) {
        this.componentes = componentes;
    }
}

