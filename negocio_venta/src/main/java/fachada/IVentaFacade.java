package fachada;

import dto.CarritoDTO;
import dto.ItemCarritoDTO;
import dto.MetodoPagoDTO;

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
     * @param metodoPago El método de pago seleccionado.
     * @return El ID del pedido creado, o null si hubo error.
     */
    String realizarPago(MetodoPagoDTO metodoPago);

    /**
     * Verifica si hay stock suficiente para todos los items del carrito.
     *
     * @return true si hay stock, false si no.
     */
    boolean verificarStockCarrito();
}

