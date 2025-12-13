package ventacontrol;

import dto.CarritoDTO;
import dto.MetodoPagoDTO;
import entidades.ConfiguracionEntidad;

import java.util.List;

/**
 * Interfaz que define la fachada para el subsistema de Venta.
 * Gestiona el carrito y el proceso de pago.
 *
 * PATRÓN: Facade
 * - Orquesta llamadas a VentaControl.
 * - Obtiene clienteId del contexto de sesión.
 * - NO mantiene estado.
 */
public interface IVentaFacade {

    /**
     * Obtiene el carrito actual del cliente.
     * Si no existe, se crea automáticamente.
     *
     * @return El carrito actual.
     */
    CarritoDTO getCarritoActual();

    /**
     * Vacía el carrito actual (elimina todos los items/configuraciones).
     */
    void vaciarCarrito();

    /**
     * Agrega un producto individual al carrito.
     *
     * @param productoId El ID del producto.
     * @param cantidad La cantidad a agregar.
     * @return true si se agregó correctamente, false si hay problemas de stock.
     */
    boolean agregarProductoAlCarrito(String productoId, int cantidad);

    /**
     * Remueve un producto del carrito.
     *
     * @param productoId El ID del producto a remover.
     */
    void removerItemDelCarrito(String productoId);

    /**
     * Actualiza la cantidad de un producto en el carrito.
     *
     * @param productoId El ID del producto.
     * @param nuevaCantidad La nueva cantidad.
     * @return true si se actualizó correctamente, false si excede el stock.
     */
    boolean actualizarCantidadItem(String productoId, int nuevaCantidad);

    /**
     * Agrega una configuración completa de PC al carrito.
     * Guarda la configuración en BD y la agrega al carrito.
     *
     * @param ensamblaje El ensamblaje/configuración a agregar.
     * @return El ID de la configuración agregada, o null si hubo error.
     */
    String agregarConfiguracionAlCarrito(dto.EnsamblajeDTO ensamblaje);

    /**
     * Remueve una configuración del carrito.
     *
     * @param configuracionId El ID de la configuración a remover.
     * @return true si se removió correctamente, false si no existe.
     */
    boolean removerConfiguracionDelCarrito(String configuracionId);

    /**
     * Obtiene todas las configuraciones que están en el carrito actual.
     *
     * @return Lista de configuraciones en el carrito.
     */
    List<ConfiguracionEntidad> obtenerConfiguracionesEnCarrito();

    /**
     * Calcula el total del carrito (productos individuales + configuraciones).
     *
     * @return El total del carrito.
     */
    double calcularTotalCarrito();

    /**
     * Verifica si hay stock suficiente para todos los items del carrito.
     *
     * @return true si hay stock, false si no.
     */
    boolean verificarStockCarrito();

    /**
     * Confirma el pedido y realiza el pago.
     * Crea el pedido, actualiza stock y vacía el carrito.
     *
     * @param metodoPago El método de pago seleccionado.
     * @return El ID del pedido creado, o null si hubo error.
     */
    String realizarPago(MetodoPagoDTO metodoPago);
}
