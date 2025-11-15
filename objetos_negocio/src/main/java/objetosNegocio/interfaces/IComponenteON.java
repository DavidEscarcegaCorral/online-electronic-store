package objetosnegocio.interfaces;

import dto.ComponenteDTO;

import java.util.List;

public interface IComponenteON {
    /**
     * Obtiene una lista de todos los componentes disponibles.
     * @return Lista de ComponenteDTO.
     */
    List<ComponenteDTO> obtenerTodos();

    /**
     * Obtiene una lista de componentes filtrados por categoría.
     * @param categoria La categoría a buscar (ej. "Procesador", "RAM").
     * @return Lista de ComponenteDTO que coinciden.
     */
    List<ComponenteDTO> obtenerPorCategoria(String categoria);

    /**
     * Obtiene un componente específico usando su ID.
     * @param id El ID único del componente.
     * @return El ComponenteDTO encontrado, o null si no existe.
     */
    ComponenteDTO obtenerPorId(String id);

    // void agregarComponente(ComponenteDTO componente);
    // boolean actualizarComponente(ComponenteDTO componente);
}
