package controlpresentacion;

import dto.ComponenteDTO;
import dto.EnsamblajeDTO;

import java.util.List;

/**
 * Interfaz para el control de presentación del flujo de armado de PC.
 *
 * Define el contrato para gestionar el estado de la vista durante
 * el proceso de selección y configuración de componentes.
 */
public interface IControlPresentacionArmado {

    /**
     * Obtiene todas las categorías disponibles de PC.
     */
    List<String> obtenerCategorias();

    /**
     * Selecciona una categoría para el armado.
     * @param categoria La categoría seleccionada (ej: "Gaming", "Office")
     */
    void seleccionarCategoria(String categoria);

    /**
     * Obtiene las marcas disponibles para la categoría actual.
     * @return Lista de marcas
     */
    List<String> obtenerMarcasParaCategoriaActual();

    /**
     * Selecciona una marca de procesador.
     * @param marca La marca seleccionada (ej: "Intel", "AMD")
     */
    void seleccionarMarca(String marca);

    /**
     * Verifica si hay productos disponibles para la categoría y marca actual.
     */
    boolean hayProductosDisponibles();

    /**
     * Obtiene los productos (componentes) disponibles para la categoría y marca seleccionadas.
     */
    List<ComponenteDTO> obtenerProductos();

    /**
     * Selecciona un producto/componente específico.
     * @param producto El componente seleccionado
     */
    void seleccionarProducto(ComponenteDTO producto);

    /**
     * Verifica si se puede avanzar al siguiente paso (si hay un componente seleccionado).
     */
    boolean puedeAvanzar();

    /**
     * Avanza al siguiente paso agregando el componente seleccionado al ensamblaje.
     * @return Lista de errores de compatibilidad (vacía si no hay errores)
     */
    List<String> avanzarConComponenteSeleccionado();

    /**
     * Verifica si se puede volver atrás desde la categoría actual.
     * @param categoria La categoría desde la cual se quiere retroceder
     */
    boolean puedeVolverAtras(String categoria);

    /**
     * Cambia un componente ya seleccionado en una categoría.
     * @param categoria La categoría del componente a cambiar
     */
    void cambiarComponente(String categoria);

    /**
     * Revalida todo el ensamblaje actual.
     * @return Lista de errores de compatibilidad encontrados
     */
    List<String> revalidarEnsamblaje();

    /**
     * Obtiene el ensamblaje (configuración) actual.
     */
    EnsamblajeDTO getEnsamblajeActual();

    /**
     * Inicia un nuevo ensamblaje limpiando el estado actual.
     */
    void iniciarNuevoEnsamblaje();

    /**
     * Obtiene la categoría actualmente seleccionada.
     */
    String getCategoriaActual();

    /**
     * Obtiene la marca actualmente seleccionada.
     */
    String getMarcaActual();

    /**
     * Obtiene el componente actualmente seleccionado.
     */
    ComponenteDTO getComponenteSeleccionado();

    /**
     * Verifica si hay un mínimo de productos disponibles para una categoría.
     * @param categoria La categoría a verificar
     * @param minimo El mínimo de productos requerido
     * @return true si hay suficientes productos, false si no
     */
    boolean tieneMinimoPorCategoria(String categoria, int minimo);

    /**
     * Verifica si existe una marca específica en una categoría.
     * @param categoria La categoría donde buscar
     * @param marca La marca a verificar
     * @return true si existe la marca, false si no
     */
    boolean tieneMarcaEnCategoria(String categoria, String marca);

    /**
     * Remueve componentes posteriores a una categoría específica en el ensamblaje.
     * @param categoria La categoría desde donde remover componentes posteriores
     */
    void removerComponentesPosteriores(String categoria);

    /**
     * Limpia completamente el ensamblaje actual.
     */
    void limpiarEnsamblaje();

    /**
     * Obtiene el componente seleccionado para una categoría específica.
     * @param categoria La categoría del componente
     * @return El componente seleccionado o null si no hay ninguno
     */
    ComponenteDTO getComponenteSeleccionado(String categoria);

    /**
     * Convierte un producto de la base de datos a ComponenteDTO.
     * @param productoId El ID del producto a convertir
     * @return El ComponenteDTO o null si no se encuentra el producto
     */
    ComponenteDTO convertirProductoADTO(String productoId);

    /**
     * Obtiene una cantidad específica de productos aleatorios para mostrar como destacados.
     *
     * @param cantidad Número de productos aleatorios a obtener
     * @return Lista de productos aleatorios como DTOs
     */
    List<ComponenteDTO> obtenerProductosAleatorios(int cantidad);
}
