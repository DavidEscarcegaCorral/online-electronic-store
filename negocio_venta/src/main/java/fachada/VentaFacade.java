package fachada;

import bo.CarritoBO;
import bo.PedidoBO;
import bo.PagoBO;
import dao.InMemoryCatalogoDAO;
import dao.InMemoryPedidoDAO;
import dao.ICatalogoDAO;
import dao.IPedidoDAO;
import dto.CarritoDTO;
import dto.CompraDTO;
import dto.ItemCarritoDTO;
import dto.ClienteDTO;
import dto.MetodoPagoDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación simple de la fachada de venta que usa BOs y DAOs en memoria.
 */
public class VentaFacade implements IVentaFacade {
    private final CarritoBO carritoBO = new CarritoBO();
    private final PedidoBO pedidoBO = new PedidoBO();
    private final PagoBO pagoBO = new PagoBO();

    private final ICatalogoDAO catalogoDAO = new InMemoryCatalogoDAO();
    private final IPedidoDAO pedidoDAO = new InMemoryPedidoDAO();

    @Override
    public CarritoDTO crearCarrito() {
        return carritoBO.crearCarrito();
    }

    @Override
    public List<String> agregarItemCarrito(CarritoDTO carrito, ItemCarritoDTO item) {
        List<String> errores = new ArrayList<>();
        // Validaciones básicas
        if (item.getCantidad() <= 0) {
            errores.add("Cantidad debe ser mayor que 0");
            return errores;
        }
        carritoBO.agregarItem(carrito, item);
        return errores;
    }

    @Override
    public CompraDTO checkout(CarritoDTO carrito, ClienteDTO cliente) {
        return pedidoBO.crearPedidoDesdeCarrito(carrito, cliente);
    }

    @Override
    public boolean confirmarPago(CompraDTO compra, MetodoPagoDTO metodo) {
        boolean ok = pagoBO.procesarPagoMock(metodo, compra);
        if (ok) {
            pedidoDAO.save(compra);
        }
        return ok;
    }
}
