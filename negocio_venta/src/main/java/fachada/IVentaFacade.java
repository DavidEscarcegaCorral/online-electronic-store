package fachada;

import dto.CarritoDTO;
import dto.ItemCarritoDTO;
import dto.MetodoPagoDTO;
import entidades.ConfiguracionEntidad;

import java.util.List;

/**
 * Interfaz que define la fachada para el subsistema de Venta.
 * Gestiona el carrito y el proceso de pago.
 */
public interface IVentaFacade {

    /**
     * Crea un nuevo carrito vacío.
     *
     * @param clienteId El ID del cliente.
     * @return El carrito creado.
     */
    CarritoDTO crearCarrito(String clienteId);

    /**
     * Obtiene el carrito actual.
     *
     * @return El carrito actual.
     */
    CarritoDTO getCarritoActual();

    /**
     * Devuelve el usuario actualmente asociado al carrito/sesión (si existe).
     */
    entidades.UsuarioEntidad getUsuarioActual();

    /**
     * Vacía el carrito actual (elimina todos los items/configuraciones y persiste el cambio).
     */
    void vaciarCarrito();

    /**
     * Agrega un item al carrito.
     *
     * @param item El item a agregar.
     * @return true si se agregó correctamente, false si hay problemas de stock.
     */
    boolean agregarItemAlCarrito(ItemCarritoDTO item);

    /**
     * Remueve un item del carrito.
     *
     * @param productoId El ID del producto a remover.
     */
    void removerItemDelCarrito(String productoId);

    /**
     * Actualiza la cantidad de un item en el carrito.
     *
     * @param productoId    El ID del producto.
     * @param nuevaCantidad La nueva cantidad.
     * @return true si se actualizó correctamente, false si excede el stock.
     */
    boolean actualizarCantidadItem(String productoId, int nuevaCantidad);

    /**
     * Calcula el total del carrito.
     *
     * @return El total del carrito.
     */
    double calcularTotalCarrito();

    /**
     * Realiza el proceso de pago (mock - happy path).
     * Crea un pedido y actualiza el stock.
     *
     * @param metodoPago El mwtodo de pago seleccionado.
     * @return El ID del pedido creado, o null si hubo error.
     */
    String realizarPago(MetodoPagoDTO metodoPago);

    /**
     * Verifica si hay stock suficiente para todos los items del carrito.
     *
     * @return true si hay stock, false si no.
     */
    boolean verificarStockCarrito();


    /**
     * Agrega una configuración completa de PC al carrito.
     * Primero guarda la configuración en BD y luego la agrega al carrito.
     *
     * @param ensamblaje El ensamblaje/configuración a agregar.
     * @return El ID de la configuración agregada, o null si hubo error.
     */
    String agregarConfiguracionAlCarrito(dto.EnsamblajeDTO ensamblaje);

    /**
     * Agrega un producto individual al carrito.
     *
     * @param productoId El ID del producto.
     * @param cantidad La cantidad a agregar.
     * @return true si se agregó correctamente, false si hubo error.
     */
    boolean agregarProductoAlCarrito(String productoId, int cantidad);

    /**
     * Obtiene todas las configuraciones que están en el carrito actual.
     *
     * @return Lista de configuraciones en el carrito.
     */
    List<ConfiguracionEntidad> obtenerConfiguracionesEnCarrito();

    /**
     * Confirma el pedido con las configuraciones del carrito.
     * Crea un pedido con todas las configuraciones, lo guarda en BD y vacía el carrito.
     *
     * @param metodoPago El método de pago seleccionado.
     * @return El ID del pedido creado, o null si hubo error.
     */
    String confirmarPedidoConConfiguraciones(MetodoPagoDTO metodoPago);


    /**
     * Remueve una configuración del carrito.
     *
     * @param configuracionId El ID de la configuración a remover.
     * @return true si se removió correctamente, false si no existe.
     */
    boolean removerConfiguracionDelCarrito(String configuracionId);
}
