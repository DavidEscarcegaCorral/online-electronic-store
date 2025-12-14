package objetosNegocio;

import objetosNegocio.PedidoBO.DireccionEntrega;
import objetosNegocio.PedidoBO.EstadoPedido;
import objetosNegocio.PedidoBO.ItemPedido;
import objetosNegocio.PedidoBO.MetodoPagoInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz para el Business Object de Pedido.
 * Define los métodos públicos para la lógica de negocio de pedidos.
 */
public interface IPedidoBO {

    String getId();

    void setId(String id);

    String getClienteId();

    void setClienteId(String clienteId);

    List<ItemPedido> getItems();

    void setItems(List<ItemPedido> items);

    BigDecimal getTotal();

    void setTotal(BigDecimal total);

    EstadoPedido getEstado();

    void setEstado(EstadoPedido estado);

    LocalDateTime getFechaCreacion();

    void setFechaCreacion(LocalDateTime fechaCreacion);

    MetodoPagoInfo getMetodoPago();

    void setMetodoPago(MetodoPagoInfo metodoPago);

    DireccionEntrega getDireccionEntrega();

    void setDireccionEntrega(DireccionEntrega direccionEntrega);

    boolean esPendiente();

    boolean estaProcesando();

    boolean estaEnviado();

    boolean estaEntregado();

    boolean estaCancelado();

    boolean puedeSerCancelado();

    void cancelar();

    void marcarComoProcesando();

    void marcarComoEnviado();

    void marcarComoEntregado();

    BigDecimal calcularTotal();

    int getCantidadTotalItems();

    void agregarItem(ItemPedido item);
}

