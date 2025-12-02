package debug;

import fachada.IVentaFacade;
import fachada.VentaFacade;
import dto.CarritoDTO;
import dto.ClienteDTO;
import dto.CompraDTO;
import dto.ItemCarritoDTO;
import dto.MetodoPagoDTO;

/**
 * Runner de prueba para el subsistema de venta.
 */
public class Runner {
    public static void main(String[] args) {
        IVentaFacade venta = new VentaFacade();

        CarritoDTO carrito = venta.crearCarrito();

        ItemCarritoDTO item = new ItemCarritoDTO();
        item.setProductoId("cpu-intel-i5");
        item.setNombre("Intel i5");
        item.setCantidad(1);
        item.setPrecioUnitario(200);

        venta.agregarItemCarrito(carrito, item);

        ClienteDTO cliente = new ClienteDTO();
        cliente.setId("cliente1");
        cliente.setNombre("Cliente Prueba");

        CompraDTO compra = venta.checkout(carrito, cliente);

        MetodoPagoDTO mp = new MetodoPagoDTO();
        mp.setTipo(MetodoPagoDTO.Tipo.TARJETA);
        mp.setDetalles("4111-xxxx-xxxx-xxxx");

        boolean ok = venta.confirmarPago(compra, mp);

        System.out.println("Pago OK: " + ok + ", pedidoId: " + compra.getId());
    }
}
