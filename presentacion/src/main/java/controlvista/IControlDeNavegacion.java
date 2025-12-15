package controlvista;


/**
 * Interfaz para el controlador principal de navegación entre pantallas.
 */
public interface IControlDeNavegacion {

    /**
     * Navega a la pantalla del Menú Principal.
     */
    void mostrarMenuPrincipal();

    /**
     * Inicia el flujo de Armado de PC (Wizard).
     */
    void mostrarArmarPc();

    /**
     * Navega a la pantalla del Carrito de Compras.
     * Debe forzar la actualización de datos del carrito.
     */
    void mostrarCarrito();

    /**
     * Muestra el detalle de un producto específico.
     * @param productoId El ID del producto a mostrar.
     */
    void mostrarProducto(String productoId);

    /**
     * Muestra el detalle de un producto usando su objeto de transferencia.
     * @param productoDTO El objeto DTO del producto.
     */
    void mostrarProducto(Object productoDTO);
}
