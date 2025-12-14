package dto;

import java.time.LocalDateTime;

/**
 * DTO para representar un pedido en la capa de presentación.
 */
public class PedidoDTO {
    private String id;
    private String clienteId;
    private double total;
    private String estado;
    private LocalDateTime fechaCreacion;

    public PedidoDTO() {
    }

    public PedidoDTO(String id, String clienteId, double total, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.total = total;
        this.estado = estado;
        this.fechaCreacion = LocalDateTime.now();
    }

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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isPendiente() {
        return "PENDIENTE".equals(estado);
    }

    public boolean isProcesando() {
        return "PROCESANDO".equals(estado);
    }

    public boolean isEnviado() {
        return "ENVIADO".equals(estado);
    }

    public boolean isEntregado() {
        return "ENTREGADO".equals(estado);
    }

    public boolean isCancelado() {
        return "CANCELADO".equals(estado);
    }
}

