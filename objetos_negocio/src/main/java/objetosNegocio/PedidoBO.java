package objetosNegocio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Business Object para Pedido.
 * Representa la lógica de negocio pura del pedido.
 * NO contiene anotaciones de MongoDB ni tipos específicos de BD.
 */
public class PedidoBO implements IPedidoBO {
    private String id;
    private String clienteId;
    private List<ItemPedido> items;
    private BigDecimal total;
    private EstadoPedido estado;
    private LocalDateTime fechaCreacion;
    private MetodoPagoInfo metodoPago;
    private DireccionEntrega direccionEntrega;

    /**
     * Estados posibles de un pedido.
     */
    public enum EstadoPedido {
        PENDIENTE,
        PROCESANDO,
        ENVIADO,
        ENTREGADO,
        CANCELADO
    }

    public PedidoBO() {
        this.items = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemPedido> getItems() {
        return items;
    }

    public void setItems(List<ItemPedido> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public MetodoPagoInfo getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPagoInfo metodoPago) {
        this.metodoPago = metodoPago;
    }

    public DireccionEntrega getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(DireccionEntrega direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    // Métodos de lógica de negocio
    public boolean esPendiente() {
        return estado == EstadoPedido.PENDIENTE;
    }

    public boolean estaProcesando() {
        return estado == EstadoPedido.PROCESANDO;
    }

    public boolean estaEnviado() {
        return estado == EstadoPedido.ENVIADO;
    }

    public boolean estaEntregado() {
        return estado == EstadoPedido.ENTREGADO;
    }

    public boolean estaCancelado() {
        return estado == EstadoPedido.CANCELADO;
    }

    public boolean puedeSerCancelado() {
        return estado == EstadoPedido.PENDIENTE || estado == EstadoPedido.PROCESANDO;
    }

    public void cancelar() {
        if (!puedeSerCancelado()) {
            throw new IllegalStateException("El pedido no puede ser cancelado en estado: " + estado);
        }
        this.estado = EstadoPedido.CANCELADO;
    }

    public void marcarComoProcesando() {
        if (!esPendiente()) {
            throw new IllegalStateException("Solo pedidos pendientes pueden ser marcados como procesando");
        }
        this.estado = EstadoPedido.PROCESANDO;
    }

    public void marcarComoEnviado() {
        if (!estaProcesando()) {
            throw new IllegalStateException("Solo pedidos en procesamiento pueden ser marcados como enviados");
        }
        this.estado = EstadoPedido.ENVIADO;
    }

    public void marcarComoEntregado() {
        if (!estaEnviado()) {
            throw new IllegalStateException("Solo pedidos enviados pueden ser marcados como entregados");
        }
        this.estado = EstadoPedido.ENTREGADO;
    }

    public BigDecimal calcularTotal() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal suma = BigDecimal.ZERO;
        for (ItemPedido item : items) {
            BigDecimal subtotal = item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad()));
            suma = suma.add(subtotal);
        }
        return suma;
    }

    public int getCantidadTotalItems() {
        if (items == null) return 0;
        return items.stream().mapToInt(ItemPedido::getCantidad).sum();
    }

    public void agregarItem(ItemPedido item) {
        if (item != null) {
            items.add(item);
        }
    }

    /**
     * Clase interna para representar un item del pedido.
     */
    public static class ItemPedido {
        private String productoId;
        private String nombre;
        private Integer cantidad;
        private BigDecimal precioUnitario;

        public ItemPedido() {
        }

        public ItemPedido(String productoId, String nombre, Integer cantidad, BigDecimal precioUnitario) {
            this.productoId = productoId;
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
        }

        public String getProductoId() {
            return productoId;
        }

        public void setProductoId(String productoId) {
            this.productoId = productoId;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public BigDecimal getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(BigDecimal precioUnitario) {
            this.precioUnitario = precioUnitario;
        }

        public BigDecimal getSubtotal() {
            if (precioUnitario == null || cantidad == null) {
                return BigDecimal.ZERO;
            }
            return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }

    /**
     * Clase interna para información del método de pago.
     */
    public static class MetodoPagoInfo {
        private String tipo;
        private String detalles;

        public MetodoPagoInfo() {
        }

        public MetodoPagoInfo(String tipo, String detalles) {
            this.tipo = tipo;
            this.detalles = detalles;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getDetalles() {
            return detalles;
        }

        public void setDetalles(String detalles) {
            this.detalles = detalles;
        }
    }

    /**
     * Clase interna para dirección de entrega.
     */
    public static class DireccionEntrega {
        private String calle;
        private String ciudad;
        private String estado;
        private String codigoPostal;
        private String pais;

        public DireccionEntrega() {
        }

        public String getCalle() {
            return calle;
        }

        public void setCalle(String calle) {
            this.calle = calle;
        }

        public String getCiudad() {
            return ciudad;
        }

        public void setCiudad(String ciudad) {
            this.ciudad = ciudad;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public String getCodigoPostal() {
            return codigoPostal;
        }

        public void setCodigoPostal(String codigoPostal) {
            this.codigoPostal = codigoPostal;
        }

        public String getPais() {
            return pais;
        }

        public void setPais(String pais) {
            this.pais = pais;
        }
    }
}

