package dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO que representa una compra/pedido.
 */
public class CompraDTO {
    private String id;
    private String clienteId;
    private List<ItemCarritoDTO> items = new ArrayList<>();
    private double total;
    private MetodoPagoDTO metodoPago;

    public CompraDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public List<ItemCarritoDTO> getItems() { return items; }
    public void setItems(List<ItemCarritoDTO> items) { this.items = items; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public MetodoPagoDTO getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPagoDTO metodoPago) { this.metodoPago = metodoPago; }
}
