package fachada;

import dto.CarritoDTO;
import dto.CompraDTO;
import dto.ItemCarritoDTO;
import dto.ClienteDTO;
import dto.MetodoPagoDTO;

import java.util.List;

/**
 * Fachada para el subsistema de ventas.
 */
public interface IVentaFacade {
    CarritoDTO crearCarrito();
    List<String> agregarItemCarrito(CarritoDTO carrito, ItemCarritoDTO item);
    CompraDTO checkout(CarritoDTO carrito, ClienteDTO cliente);
    boolean confirmarPago(CompraDTO compra, MetodoPagoDTO metodo);
}
