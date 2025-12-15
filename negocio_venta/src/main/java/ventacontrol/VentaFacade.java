package ventacontrol;

import dto.EnsamblajeDTO;
import ensamblajecontrol.ConfiguracionFacade;
import ensamblajecontrol.IEnsamblajeFacade;
import dao.UsuarioDAO;
import dto.MetodoPagoDTO;
import entidades.ConfiguracionEntidad;
import entidades.UsuarioEntidad;
import java.util.List;

/**
 * Implementación de la fachada para el subsistema de Venta.
 * PATRÓN: Facade
 */
public class VentaFacade implements IVentaFacade {
    private final VentaControl ventaControl;
    private final UsuarioDAO usuarioDAO;

    public VentaFacade() {
        this.ventaControl = VentaControl.getInstance();
        this.usuarioDAO = new UsuarioDAO();
    }

    public static VentaFacade getInstance() {
        return new VentaFacade();
    }

    private String obtenerClienteIdDefecto() {
        try {
            UsuarioEntidad usuario = usuarioDAO.obtenerPorEmail("cliente_default@local");
            if (usuario != null && usuario.getId() != null) {
                return usuario.getId().toString();
            }
            throw new IllegalStateException("Usuario por defecto no encontrado.");
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo obtener el cliente por defecto", e);
        }
    }

    @Override
    public void removerItemDelCarrito(String productoId) {
        String clienteId = obtenerClienteIdDefecto();
        ventaControl.removerItemDelCarrito(clienteId, productoId);
    }

    @Override
    public boolean actualizarCantidadItem(String productoId, int nuevaCantidad) {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.actualizarCantidadItem(clienteId, productoId, nuevaCantidad);
    }

    @Override
    public double calcularTotalCarrito() {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.calcularTotalCarrito(clienteId);
    }

    @Override
    public String realizarPago(MetodoPagoDTO metodoPago) {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.confirmarPedido(clienteId, metodoPago);
    }

    @Override
    public void vaciarCarrito() {
        String clienteId = obtenerClienteIdDefecto();
        ventaControl.vaciarCarrito(clienteId);
    }

    @Override
    public boolean verificarStockCarrito() {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.verificarStockCarrito(clienteId);
    }

    @Override
    public String agregarConfiguracionAlCarrito(EnsamblajeDTO ensamblaje) {
        String clienteId = obtenerClienteIdDefecto();

        IEnsamblajeFacade fachadaControl = ConfiguracionFacade.getInstance();
        String configuracionId = fachadaControl.guardarConfiguracion(ensamblaje, clienteId);

        if (configuracionId == null) {
            return null;
        }

        return ventaControl.agregarConfiguracionAlCarrito(clienteId, configuracionId);
    }

    @Override
    public boolean agregarProductoAlCarrito(String productoId, int cantidad) {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.agregarProductoAlCarrito(clienteId, productoId, cantidad);
    }


    @Override
    public objetosNegocio.CarritoBO obtenerCarrito() {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.obtenerCarrito(clienteId);
    }

    @Override
    public List<ConfiguracionEntidad> obtenerConfiguracionesEnCarrito() {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.obtenerConfiguracionesEnCarrito(clienteId);
    }

    @Override
    public boolean removerConfiguracionDelCarrito(String configuracionId) {
        String clienteId = obtenerClienteIdDefecto();
        return ventaControl.removerConfiguracionDelCarrito(clienteId, configuracionId);
    }

}
