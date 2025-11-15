package dto;

import java.util.HashMap;
import java.util.Map;

public class EnsamblajeDTO {
    private Map<String, ComponenteDTO> componentes;

    public EnsamblajeDTO() {
        this.componentes = new HashMap<>();
    }

    /**
     * Añade o reemplaza un componente en el ensamblaje.
     * @param categoria La categoría (ej. "Procesador")
     * @param componente El componente seleccionado
     */
    public void setComponente(String categoria, ComponenteDTO componente) {
        componentes.put(categoria, componente);
    }

    /**
     * Obtiene el componente de una categoría específica.
     */
    public ComponenteDTO getComponente(String categoria) {
        return componentes.get(categoria);
    }

    /**
     * Obtiene el mapa completo de componentes.
     */
    public Map<String, ComponenteDTO> getComponentes() {
        return componentes;
    }

    /**
     * Calcula el precio total sumando el precio de todos los componentes.
     */
    public double getPrecioTotal() {
        double total = 0;
        for (ComponenteDTO comp : componentes.values()) {
            if (comp != null) {
                total += comp.getPrecioCompra();
            }
        }
        return total;
    }
}
