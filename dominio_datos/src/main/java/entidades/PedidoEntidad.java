package entidades;

import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa un pedido en MongoDB.
 *
 * NOTA: Usa BigDecimal para el total (evita problemas de precisión de Double).
 * NOTA: Usa LocalDateTime para fechaCreacion (API moderna de Java 8+).
 * NOTA: Usa EstadoPedido enum para el estado (evita errores de tipeo).
 */
public class PedidoEntidad {
    private ObjectId id;
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
        PENDIENTE,      // Pedido creado pero no procesado
        PROCESANDO,     // Pedido en proceso de preparación
        ENVIADO,        // Pedido enviado al cliente
        ENTREGADO,      // Pedido entregado exitosamente
        CANCELADO       // Pedido cancelado por el cliente o sistema
    }

    public PedidoEntidad() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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
        this.items = items;
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

    /**
     * Representa un item individual dentro del pedido.
     */
    public static class ItemPedido {
        private String productoId;
        private String nombre;
        private Integer cantidad;
        private BigDecimal precioUnitario;

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
    }

    /**
     * Información del método de pago utilizado en el pedido.
     */
    public static class MetodoPagoInfo {
        private String tipo;
        private String detalles;

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
     * Dirección de entrega del pedido.
     */
    public static class DireccionEntrega {
        private String calle;
        private String ciudad;
        private String estado;
        private String codigoPostal;

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
    }
}

