package controlpresentacion;

import controlconfig.IVentaFacade;
import controlconfig.VentaFacade;
import dto.MetodoPagoDTO;
import entidades.ConfiguracionEntidad;

import java.util.List;

public class ControlPresentacionVenta {
    private static ControlPresentacionVenta instancia;
    private final IVentaFacade ventaFacade;

    private ControlPresentacionVenta() {
        this.ventaFacade = VentaFacade.getInstance();
    }

    public static synchronized ControlPresentacionVenta getInstance() {
        if (instancia == null) {
            instancia = new ControlPresentacionVenta();
        }
        return instancia;
    }

    public void vaciarCarrito() {
        ventaFacade.vaciarCarrito();
    }

    public double calcularTotalCarrito() {
        return ventaFacade.calcularTotalCarrito();
    }

    public List<ConfiguracionEntidad> obtenerConfiguracionesEnCarrito() {
        return ventaFacade.obtenerConfiguracionesEnCarrito();
    }

    public String confirmarPedido(MetodoPagoDTO metodoPago) {
        return ventaFacade.confirmarPedidoConConfiguraciones(metodoPago);
    }

    public boolean agregarProductoAlCarrito(String productoId, int cantidad) {
        return ventaFacade.agregarProductoAlCarrito(productoId, cantidad);
    }

    public String agregarConfiguracionAlCarrito(dto.EnsamblajeDTO ensamblaje) {
        return ventaFacade.agregarConfiguracionAlCarrito(ensamblaje);
    }

    public entidades.UsuarioEntidad getUsuarioActual() {
        return ventaFacade.getUsuarioActual();
    }
}

