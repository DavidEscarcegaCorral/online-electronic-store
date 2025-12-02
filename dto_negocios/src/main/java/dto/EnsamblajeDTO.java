package dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * DTO que representa un ensamblaje (configuración temporal de componentes).
 */
public class EnsamblajeDTO {
    private Map<String, ComponenteDTO> componentes = new HashMap<>();

    public EnsamblajeDTO() {}

    public void setComponente(String categoria, ComponenteDTO componente) {
        this.componentes.put(categoria, componente);
    }

    public ComponenteDTO getComponente(String categoria) {
        return this.componentes.get(categoria);
    }

    public Map<String, ComponenteDTO> getComponentes() {
        return Collections.unmodifiableMap(componentes);
    }

    public double getPrecioTotal() {
        return componentes.values().stream().mapToDouble(ComponenteDTO::getPrecio).sum();
    }
}
