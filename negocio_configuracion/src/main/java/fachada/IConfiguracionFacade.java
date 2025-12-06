package fachada;

import dto.ComponenteDTO;

import java.util.List;

/**
 * Interfaz que define la fachada para el subsistema de Configuración.
 * Gestiona el flujo secuencial: Categoría -> Marca -> Productos.
 */
public interface IConfiguracionFacade {

    /**
     * Obtiene la lista de categorías disponibles.
     *
     * @return Lista de nombres de categorías.
     */
    List<String> obtenerCategorias();

    /**
     * Obtiene las marcas disponibles para una categoría específica.
     *
     * @param categoria La categoría seleccionada.
     * @return Lista de marcas disponibles.
     */
    List<String> obtenerMarcasPorCategoria(String categoria);

    /**
     * Obtiene los productos que coinciden con la categoría y marca seleccionadas.
     *
     * @param categoria La categoría seleccionada.
     * @param marca     La marca seleccionada.
     * @return Lista de productos disponibles.
     */
    List<ComponenteDTO> obtenerProductosPorCategoriaYMarca(String categoria, String marca);

    /**
     * Verifica si hay productos disponibles para la combinación categoría-marca.
     *
     * @param categoria La categoría seleccionada.
     * @param marca     La marca seleccionada.
     * @return true si hay productos, false si no hay.
     */
    boolean hayProductosDisponibles(String categoria, String marca);
}

