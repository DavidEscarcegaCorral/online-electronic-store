package controlpresentacion;

import dto.ComponenteDTO;
import dto.EnsamblajeDTO;

import java.util.List;

/**
 * Interfaz que define el contrato para el control de presentación.
 * Gestiona el estado de la configuración de PC en la interfaz gráfica.
 */
public interface IControlPresentacion {

    /**
     * Obtiene la lista de categorías disponibles.
     *
     * @return Lista de nombres de categorías.
     */
    List<String> obtenerCategorias();

    /**
     * Selecciona una categoría y resetea los estados dependientes.
     *
     * @param categoria La categoría a seleccionar.
     */
    void seleccionarCategoria(String categoria);

    /**
     * Obtiene las marcas disponibles para la categoría actualmente seleccionada.
     *
     * @return Lista de marcas disponibles.
     * @throws IllegalStateException si no hay categoría seleccionada.
     */
    List<String> obtenerMarcasParaCategoriaActual();

    /**
     * Selecciona una marca para la categoría actualmente seleccionada.
     *
     * @param marca La marca a seleccionar.
     */
    void seleccionarMarca(String marca);

    /**
     * Verifica si hay productos disponibles para la categoría y marca seleccionadas.
     *
     * @return true si hay productos disponibles, false en caso contrario.
     */
    boolean hayProductosDisponibles();

    /**
     * Obtiene los productos disponibles para la categoría y marca seleccionadas.
     *
     * @return Lista de productos disponibles.
     * @throws IllegalStateException si no hay categoría o marca seleccionadas.
     */
    List<ComponenteDTO> obtenerProductos();

    /**
     * Selecciona un producto de la lista de productos disponibles.
     *
     * @param producto El producto a seleccionar.
     */
    void seleccionarProducto(ComponenteDTO producto);

    /**
     * Verifica si se puede avanzar con el producto seleccionado.
     *
     * @return true si hay un producto seleccionado, false en caso contrario.
     */
    boolean puedeAvanzar();

    /**
     * Avanza en el flujo agregando el componente seleccionado al ensamblaje.
     * Resetea los estados si la operación es exitosa.
     *
     * @return Lista de errores encontrados. Lista vacía si fue exitosa.
     * @throws IllegalStateException si no hay componente seleccionado.
     */
    List<String> avanzarConComponenteSeleccionado();

    /**
     * Verifica si se puede volver atrás para cambiar un componente de una categoría.
     *
     * @param categoria La categoría a validar.
     * @return true si se puede volver atrás, false en caso contrario.
     */
    boolean puedeVolverAtras(String categoria);

    /**
     * Cambia un componente de una categoría específica.
     * Resetea los estados relacionados a esa categoría.
     *
     * @param categoria La categoría del componente a cambiar.
     */
    void cambiarComponente(String categoria);

    /**
     * Revalida el ensamblaje actual para verificar compatibilidades.
     *
     * @return Lista de errores encontrados.
     */
    List<String> revalidarEnsamblaje();

    /**
     * Obtiene el ensamblaje actual con todos los componentes seleccionados.
     *
     * @return El ensamblaje actual.
     */
    EnsamblajeDTO getEnsamblajeActual();

    /**
     * Inicia un nuevo ensamblaje, reseteando todos los estados.
     */
    void iniciarNuevoEnsamblaje();

    /**
     * Obtiene la categoría actualmente seleccionada.
     *
     * @return La categoría actual, o null si no hay seleccionada.
     */
    String getCategoriaActual();

    /**
     * Obtiene la marca actualmente seleccionada.
     *
     * @return La marca actual, o null si no hay seleccionada.
     */
    String getMarcaActual();

    /**
     * Obtiene el componente actualmente seleccionado.
     *
     * @return El componente seleccionado, o null si no hay seleccionado.
     */
    ComponenteDTO getComponenteSeleccionado();
}

