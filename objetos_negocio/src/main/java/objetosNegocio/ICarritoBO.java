package objetosNegocio;

import objetosNegocio.CarritoBO.ItemCarritoBO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interfaz para el Business Object de Carrito de Compras.
 * Define los métodos públicos para la lógica de negocio del carrito.
 */
public interface ICarritoBO {

    String getId();

    void setId(String id);

    String getClienteId();

    void setClienteId(String clienteId);

    List<ItemCarritoBO> getProductos();

    void setProductos(List<ItemCarritoBO> productos);

    List<String> getConfiguracionesIds();

    void setConfiguracionesIds(List<String> configuracionesIds);

    boolean estaVacio();

    int getCantidadProductos();

    int getCantidadConfiguraciones();

    int getCantidadTotalItems();

    BigDecimal calcularSubtotal();

    void agregarProducto(String productoId, String nombre, BigDecimal precio, int cantidad);

    void removerProducto(String productoId);

    void actualizarCantidadProducto(String productoId, int nuevaCantidad);

    void agregarConfiguracion(String configuracionId);

    void removerConfiguracion(String configuracionId);

    void vaciar();

    boolean tieneProducto(String productoId);

    boolean tieneConfiguracion(String configuracionId);
}

