package controlconfig;

import dto.MetodoPagoDTO;
import entidades.ConfiguracionEntidad;
import java.util.List;

/**
 * Interfaz que define el contrato para el control de venta.
 * Define todas las operaciones de carrito y pedidos.
 */
public interface IVentaControl {

    /**
     * Agrega una configuración guardada al carrito del usuario.
     *
     * @param configuracionId El ID de la configuración a agregar.
     * @return El ID de la configuración, o null si hubo error.
     */
    String agregarConfiguracionAlCarrito(String configuracionId);

    /**
     * Agrega un producto individual al carrito.
     *
     * @param productoId El ID del producto a agregar.
     * @param cantidad La cantidad a agregar.
     * @return true si se agregó correctamente, false si hubo error.
     */
    boolean agregarProductoAlCarrito(String productoId, int cantidad);

    /**
     * Vacía completamente el carrito del usuario.
     * Elimina todas las configuraciones y limpia las referencias en la BD.
     */
    void vaciarCarrito();

    /**
     * Calcula el precio total del carrito.
     *
     * @return El total del carrito.
     */
    double calcularTotalCarrito();

    /**
     * Obtiene todas las configuraciones que están en el carrito actual.
     *
     * @return Lista de configuraciones en el carrito.
     */
    List<ConfiguracionEntidad> obtenerConfiguracionesEnCarrito();

    /**
     * Confirma el pedido con las configuraciones del carrito.
     * Crea un nuevo pedido, lo guarda en BD y vacía el carrito.
     *
     * @param metodoPago El método de pago seleccionado.
     * @return El ID del pedido creado, o null si hubo error.
     */
    String confirmarPedido(MetodoPagoDTO metodoPago);
}

