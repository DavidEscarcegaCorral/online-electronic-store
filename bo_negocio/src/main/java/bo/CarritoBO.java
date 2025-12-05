package bo;

import dto.CarritoDTO;
import dto.ItemCarritoDTO;

/**
 * Lógica de negocio para manejo de carrito.
 */
public class CarritoBO {
    public CarritoBO() {}

    public CarritoDTO crearCarrito() {
        return new CarritoDTO();
    }

    public void agregarItem(CarritoDTO carrito, ItemCarritoDTO item) {
        // Usar la API del DTO para modificar el carrito
        carrito.agregarItem(item);
    }

    public void removerItem(CarritoDTO carrito, ItemCarritoDTO item) {
        carrito.removerItem(item);
    }
}
