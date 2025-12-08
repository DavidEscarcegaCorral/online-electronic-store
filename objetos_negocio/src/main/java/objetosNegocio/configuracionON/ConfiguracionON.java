package objetosNegocio.configuracionON;

import java.util.List;
import java.util.Map;

public class ConfiguracionON implements IConfiguracionON {
    private String id;
    private String nombre;
    private List<Map<String, Object>> componentes;
    private Double precioTotal;

    public ConfiguracionON() {}

    public ConfiguracionON(String id, String nombre, List<Map<String, Object>> componentes, Double precioTotal) {
        this.id = id;
        this.nombre = nombre;
        this.componentes = componentes;
        this.precioTotal = precioTotal;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
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

    @Override
    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }
}

