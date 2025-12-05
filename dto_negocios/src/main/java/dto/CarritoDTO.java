package dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO para representar un carrito de compras.
 */
public class CarritoDTO {
    private List<ItemCarritoDTO> items = new ArrayList<>();

    public CarritoDTO() {}

    public void agregarItem(ItemCarritoDTO item) { this.items.add(item); }
    public void removerItem(ItemCarritoDTO item) { this.items.remove(item); }
    public List<ItemCarritoDTO> getItems() { return Collections.unmodifiableList(items); }

    public double getTotal() { return items.stream().mapToDouble(ItemCarritoDTO::getSubtotal).sum(); }
}
