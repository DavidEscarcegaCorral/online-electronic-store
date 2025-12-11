package fachada;

import dto.ComponenteDTO;
import dto.EnsamblajeDTO;

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

    /**
     * Aplica (recibe) una configuración completa representada por un EnsamblajeDTO.
     * El metodo intentará cargar la configuración en el subsistema de armado y
     * devolverá una lista de errores de compatibilidad o validación si existen.
     *
     * @param ensamblaje Ensamblaje completo con componentes por categoría.
     * @return Lista de errores detectados; si está vacía, la configuración es válida.
     */
    List<String> aplicarConfiguracion(EnsamblajeDTO ensamblaje);

    /**
     * Guarda una configuración completa en la base de datos.
     * Este método persiste un ensamblaje en la colección de configuraciones.
     *
     * @param ensamblaje El ensamblaje/configuración a guardar.
     * @return El ID de la configuración guardada, o null si hubo error.
     */
    String guardarConfiguracion(EnsamblajeDTO ensamblaje);

    /**
     * Verifica si hay al menos `minimo` productos disponibles (stock>0) para la categoría.
     *
     * @param categoria categoría a verificar
     * @param minimo cantidad mínima requerida
     * @return true si hay al menos `minimo` productos disponibles
     */
    boolean tieneMinimoPorCategoria(String categoria, int minimo);

    /**
     * Verifica si hay productos disponibles para una marca específica en una categoría.
     *
     * @param categoria La categoría a verificar
     * @param marca La marca a verificar
     * @return true si hay al menos un producto disponible con esa marca y categoría
     */
    boolean tieneMarcaEnCategoria(String categoria, String marca);
}
