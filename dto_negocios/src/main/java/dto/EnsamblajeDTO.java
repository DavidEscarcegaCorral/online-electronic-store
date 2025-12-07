package dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO que representa un ensamblaje (configuración temporal de componentes).
 */
public class EnsamblajeDTO {
    private Map<String, ComponenteDTO> componentes = new HashMap<>();

    public EnsamblajeDTO() {
    }

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

    /**
     * Remueve un componente del ensamblaje.
     *
     * @param categoria La categoría del componente a remover.
     */
    public void removerComponente(String categoria) {
        this.componentes.remove(categoria);
    }

    /**
     * Obtiene todos los componentes como lista.
     *
     * @return Lista de componentes en el ensamblaje.
     */
    public List<ComponenteDTO> obtenerTodosComponentes() {
        return new ArrayList<>(componentes.values());
    }

    /**
     * Verifica si el ensamblaje tiene un componente de la categoría especificada.
     *
     * @param categoria La categoría a verificar.
     * @return true si existe, false si no.
     */
    public boolean tieneComponente(String categoria) {
        return this.componentes.containsKey(categoria);
    }
}


