package dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para representar un carrito de compras.
 */
public class CarritoDTO {
    private String clienteId;
    private List<ItemCarritoDTO> items = new ArrayList<>();

    public CarritoDTO() {
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public void agregarItem(ItemCarritoDTO item) {
        this.items.add(item);
    }

    public void removerItem(ItemCarritoDTO item) {
        this.items.remove(item);
    }

    public List<ItemCarritoDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemCarritoDTO> items) {
        this.items = items;
    }

    public double getTotal() {
        return items.stream().mapToDouble(ItemCarritoDTO::getSubtotal).sum();
    }
}
