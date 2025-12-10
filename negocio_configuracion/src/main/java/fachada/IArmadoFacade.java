package fachada;

import dto.ComponenteDTO;
import dto.EnsamblajeDTO;

import java.util.List;

public interface IArmadoFacade {

    EnsamblajeDTO iniciarNuevoEnsamblaje();

    EnsamblajeDTO getEnsamblajeActual();

    List<ComponenteDTO> obtenerComponentesPorCategoria(String categoria);

    List<String> agregarComponente(ComponenteDTO componente);

    boolean verificarStockSuficiente(String tipoUso);

    List<ComponenteDTO> obtenerComponentesCompatibles(String categoria, String tipoUso);

    boolean puedeVolverAtras(String categoria);

    void removerComponente(String categoria);

    List<String> revalidarEnsamblaje();

    /**
     * Obtiene el componente actualmente seleccionado en el ensamblaje para la categoría indicada.
     * Devuelve null si no hay componente para esa categoría.
     */
    ComponenteDTO getComponenteSeleccionado(String categoria);

    void limpiarEnsamblaje();

    /**
     * Remueve todos los componentes posteriores a una categoría específica en el orden de construcción.
     * Útil cuando el usuario retrocede para cambiar un componente previo.
     *
     * @param categoria La categoría desde donde se eliminan componentes posteriores
     */
    void removerComponentesPosteriores(String categoria);
}
