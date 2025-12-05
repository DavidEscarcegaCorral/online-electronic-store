package fachada;

import dto.ComponenteDTO;
import dto.EnsamblajeDTO;

import java.util.List;

/**
 * Interfaz que define la fachada para el subsistema de Armado de Computadora.
 * Es el único punto de entrada para el controlador.
 */
public interface IArmadoFacade {

    /**
     * Inicia un nuevo proceso de ensamblaje, creando un DTO vacío.
     * 
     * @return El EnsamblajeDTO nuevo.
     */
    EnsamblajeDTO iniciarNuevoEnsamblaje();

    /**
     * Obtiene el ensamblaje que se está construyendo actualmente.
     * 
     * @return El EnsamblajeDTO actual.
     */
    EnsamblajeDTO getEnsamblajeActual();

    /**
     * Obtiene la lista de componentes filtrados por una categoría.
     * 
     * @param categoria La categoría a buscar (ej. "Procesador").
     * @return Una lista de ComponenteDTO.
     */
    List<ComponenteDTO> obtenerComponentesPorCategoria(String categoria);

    /**
     * Intenta agregar un componente al ensamblaje actual.
     * Primero valida la compatibilidad.
     * * @param componente El componente a agregar.
     * 
     * @return Una lista de mensajes de error. Si la lista está vacía,
     *         el componente se agregó con éxito.
     */
    List<String> agregarComponente(ComponenteDTO componente);

    /**
     * Verifica si hay stock suficiente para armar una PC completa para el tipo de
     * uso dado.
     * 
     * @param tipoUso El tipo de uso (Gamer, Office, etc.)
     * @return true si hay stock suficiente, false en caso contrario.
     */
    boolean verificarStockSuficiente(String tipoUso);

    /**
     * Obtiene componentes compatibles con el ensamblaje actual y el tipo de uso.
     * 
     * @param categoria La categoría a buscar.
     * @param tipoUso   El tipo de uso seleccionado.
     * @return Lista de componentes compatibles.
     */
    List<ComponenteDTO> obtenerComponentesCompatibles(String categoria, String tipoUso);
}
