package bo;

import dto.CarritoDTO;
import dto.CompraDTO;
import dto.ItemCarritoDTO;
import dto.ClienteDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Lógica de negocio para transformación carrito -> pedido.
 */
public class PedidoBO {
    public CompraDTO crearPedidoDesdeCarrito(CarritoDTO carrito, ClienteDTO cliente) {
        CompraDTO compra = new CompraDTO();
        compra.setClienteId(cliente != null ? cliente.getId() : null);
        List<ItemCarritoDTO> items = new ArrayList<>(carrito.getItems());
        compra.setItems(items);
        compra.setTotal(carrito.getTotal());
        return compra;
    }
}
