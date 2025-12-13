package controlpresentacion;

import ventacontrol.IVentaFacade;
import ventacontrol.VentaFacade;
import dto.ConfiguracionDTO;
import dto.EnsamblajeDTO;
import dto.MetodoPagoDTO;
import entidades.ConfiguracionEntidad;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del control de presentación para gestionar ventas y carritos.
 *
 * RESPONSABILIDADES:
 * - Convierte Entidades (de BD) a DTOs (para la vista).
 * - La vista NUNCA debe recibir objetos de entidad directamente.
 * - Orquesta llamadas a la fachada de ventas.
 *
 * NOTA: Puede ser Singleton ya que no mantiene estado mutable de sesión.
 */
public class ControlPresentacionVenta implements IControlPresentacionVenta {
    private static ControlPresentacionVenta instancia;
    private final IVentaFacade ventaFacade;

    /**
     * Constructor para inyección de dependencias (usado en pruebas).
     * @param ventaFacade La fachada a utilizar (puede ser un mock).
     */
    public ControlPresentacionVenta(IVentaFacade ventaFacade) {
        this.ventaFacade = ventaFacade;
    }

    /**
     * Constructor privado para uso en producción (Singleton).
     * Llama al constructor público con la implementación real.
     */
    private ControlPresentacionVenta() {
        this(VentaFacade.getInstance());
    }

    public static synchronized ControlPresentacionVenta getInstance() {
        if (instancia == null) {
            instancia = new ControlPresentacionVenta();
        }
        return instancia;
    }

    @Override
    public void vaciarCarrito() {
        ventaFacade.vaciarCarrito();
    }

    @Override
    public double calcularTotalCarrito() {
        return ventaFacade.calcularTotalCarrito();
    }

    @Override
    public List<ConfiguracionDTO> obtenerConfiguracionesEnCarrito() {
        List<ConfiguracionEntidad> entidades = ventaFacade.obtenerConfiguracionesEnCarrito();

        if (entidades == null || entidades.isEmpty()) {
            return new ArrayList<>();
        }

        return entidades.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public String confirmarPedido(MetodoPagoDTO metodoPago) {
        return ventaFacade.realizarPago(metodoPago);
    }

    @Override
    public boolean agregarProductoAlCarrito(String productoId, int cantidad) {
        return ventaFacade.agregarProductoAlCarrito(productoId, cantidad);
    }

    @Override
    public String agregarConfiguracionAlCarrito(EnsamblajeDTO ensamblaje) {
        return ventaFacade.agregarConfiguracionAlCarrito(ensamblaje);
    }

    /**
     * Convierte una ConfiguracionEntidad (BD) a ConfiguracionDTO (Vista).
     * Evita la fuga de entidades a la capa de presentación.
     */
    private ConfiguracionDTO convertirADTO(ConfiguracionEntidad entidad) {
        ConfiguracionDTO dto = new ConfiguracionDTO();

        if (entidad.getId() != null) {
            dto.setId(entidad.getId().toString());
        }

        dto.setNombre(entidad.getNombre());
        dto.setUsuarioId(entidad.getUsuarioId());
        dto.setPrecioTotal(entidad.getPrecioTotal());
        dto.setComponentes(entidad.getComponentes());

        return dto;
    }
}
