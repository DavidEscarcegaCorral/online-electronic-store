package controlpresentacion;

import dto.ConfiguracionDTO;
import dto.EnsamblajeDTO;
import dto.MetodoPagoDTO;
import dto.ItemCarritoDTO;
import java.util.List;

/**
 * Interfaz para el control de presentación de ventas y carrito.
 *
 * Define el contrato para gestionar las operaciones relacionadas
 * con el carrito de compras y el proceso de checkout.
 */
public interface IControlPresentacionVenta {

    /**
     * Vacía completamente el carrito de compras.
     */
    void vaciarCarrito();

    /**
     * Calcula el total del carrito (configuraciones + productos individuales).
     * @return El monto total del carrito
     */
    double calcularTotalCarrito();

    /**
     * Obtiene todas las configuraciones de PC que están en el carrito.
     * Retorna DTOs para evitar fugas de entidades a la vista.
     *
     * @return Lista de configuraciones como DTOs
     */
    List<ConfiguracionDTO> obtenerConfiguracionesEnCarrito();

    /**
     * Obtiene todos los productos individuales del carrito.
     *
     * @return Lista de items del carrito como DTOs
     */
    List<ItemCarritoDTO> obtenerProductosDelCarrito();

    /**
     * Confirma el pedido y procesa el pago.
     *
     * @param metodoPago El método de pago seleccionado por el usuario
     * @return El ID del pedido creado, o null si hubo error
     */
    String confirmarPedido(MetodoPagoDTO metodoPago);

    /**
     * Agrega un producto individual al carrito.
     *
     * @param productoId El ID del producto a agregar
     * @param cantidad La cantidad del producto
     * @return true si se agregó correctamente, false si hubo error o no hay stock
     */
    boolean agregarProductoAlCarrito(String productoId, int cantidad);

    /**
     * Remueve un producto del carrito por su id.
     *
     * @param productoId El id del producto a remover
     */
    void removerProductoDelCarrito(String productoId);

    /**
     * Actualiza la cantidad de un producto que ya está en el carrito.
     * @param productoId id del producto
     * @param nuevaCantidad nueva cantidad solicitada
     * @return true si la actualización fue aceptada, false si excede stock u otro error
     */
    boolean actualizarCantidadItem(String productoId, int nuevaCantidad);

    /**
     * Verifica si todas las cantidades del carrito son válidas frente al inventario.
     * @return true si hay stock suficiente para todos los items
     */
    boolean verificarStockCarrito();

    /**
     * Agrega una configuración completa de PC al carrito.
     *
     * @param ensamblaje El ensamblaje/configuración a agregar
     * @return El ID de la configuración guardada, o null si hubo error
     */
    String agregarConfiguracionAlCarrito(EnsamblajeDTO ensamblaje);
}
