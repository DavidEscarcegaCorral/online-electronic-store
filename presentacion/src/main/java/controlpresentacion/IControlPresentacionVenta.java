package controlpresentacion;

import dto.ConfiguracionDTO;
import dto.EnsamblajeDTO;
import dto.MetodoPagoDTO;

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
     * Agrega una configuración completa de PC al carrito.
     *
     * @param ensamblaje El ensamblaje/configuración a agregar
     * @return El ID de la configuración guardada, o null si hubo error
     */
    String agregarConfiguracionAlCarrito(EnsamblajeDTO ensamblaje);
}

