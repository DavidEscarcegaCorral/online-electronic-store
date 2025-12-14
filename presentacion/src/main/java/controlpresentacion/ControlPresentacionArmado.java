package controlpresentacion;

import ensamblajecontrol.ConfiguracionFacade;
import ensamblajecontrol.IEnsamblajeFacade;
import dto.ComponenteDTO;
import dto.EnsamblajeDTO;

import java.util.List;

/**
 * Implementación del control de presentación para el flujo de armado de PC.
 *
 * IMPORTANTE: Esta clase NO debe ser Singleton.
 * Cada instancia de la vista debe tener su propia instancia del controlador
 * para evitar conflictos de estado entre múltiples usuarios/sesiones.
 */
public class ControlPresentacionArmado implements IControlPresentacionArmado {

    private final IEnsamblajeFacade fachada;

    private String categoriaActual;
    private String marcaActual;
    private ComponenteDTO componenteSeleccionado;

    public ControlPresentacionArmado() {
        this.fachada = new ConfiguracionFacade();
    }

    @Override
    public List<String> obtenerCategorias() {
        return fachada.obtenerCategorias();
    }

    @Override
    public void seleccionarCategoria(String categoria) {
        this.categoriaActual = categoria;
        this.marcaActual = null;
        this.componenteSeleccionado = null;

        ComponenteDTO seleccionado = fachada.getComponenteSeleccionado(categoria);
        if (seleccionado != null) {
            this.marcaActual = seleccionado.getMarca();
            this.componenteSeleccionado = seleccionado;
        }
    }

    @Override
    public List<String> obtenerMarcasParaCategoriaActual() {
        if (categoriaActual == null) {
            throw new IllegalStateException("Debe seleccionar una categoría primero");
        }
        return fachada.obtenerMarcasPorCategoria(categoriaActual);
    }

    @Override
    public void seleccionarMarca(String marca) {
        this.marcaActual = marca;
        this.componenteSeleccionado = null;
    }

    @Override
    public boolean hayProductosDisponibles() {
        if (categoriaActual == null || marcaActual == null) {
            return false;
        }
        return fachada.hayProductosDisponibles(categoriaActual, marcaActual);
    }

    @Override
    public List<ComponenteDTO> obtenerProductos() {
        if (categoriaActual == null || marcaActual == null) {
            throw new IllegalStateException("Debe seleccionar categoría y marca primero");
        }
        return fachada.obtenerProductosPorCategoriaYMarca(categoriaActual, marcaActual, fachada.getEnsamblajeActual());
    }

    @Override
    public void seleccionarProducto(ComponenteDTO producto) {
        this.componenteSeleccionado = producto;
    }

    @Override
    public boolean puedeAvanzar() {
        return componenteSeleccionado != null;
    }

    @Override
    public List<String> avanzarConComponenteSeleccionado() {
        if (componenteSeleccionado == null) {
            throw new IllegalStateException("No hay componente seleccionado");
        }

        List<String> errores = fachada.agregarComponente(componenteSeleccionado);

        if (errores.isEmpty()) {
            this.categoriaActual = null;
            this.marcaActual = null;
            this.componenteSeleccionado = null;
        }

        return errores;
    }

    @Override
    public boolean puedeVolverAtras(String categoria) {
        return fachada.puedeVolverAtras(categoria);
    }

    @Override
    public void cambiarComponente(String categoria) {
        fachada.removerComponente(categoria);
        this.categoriaActual = categoria;
        this.marcaActual = null;
        this.componenteSeleccionado = null;
    }

    @Override
    public List<String> revalidarEnsamblaje() {
        return fachada.revalidarEnsamblaje();
    }

    @Override
    public EnsamblajeDTO getEnsamblajeActual() {
        return fachada.getEnsamblajeActual();
    }

    @Override
    public void iniciarNuevoEnsamblaje() {
        fachada.iniciarNuevoEnsamblaje();
        this.categoriaActual = null;
        this.marcaActual = null;
        this.componenteSeleccionado = null;
    }

    @Override
    public String getCategoriaActual() {
        return categoriaActual;
    }

    @Override
    public String getMarcaActual() {
        return marcaActual;
    }

    @Override
    public ComponenteDTO getComponenteSeleccionado() {
        return componenteSeleccionado;
    }

    @Override
    public boolean tieneMinimoPorCategoria(String categoria, int minimo) {
        return fachada.tieneMinimoPorCategoria(categoria, minimo);
    }

    @Override
    public boolean tieneMarcaEnCategoria(String categoria, String marca) {
        return fachada.tieneMarcaEnCategoria(categoria, marca);
    }

    @Override
    public void removerComponentesPosteriores(String categoria) {
        fachada.removerComponentesPosteriores(categoria);
        this.categoriaActual = null;
        this.marcaActual = null;
        this.componenteSeleccionado = null;
    }

    @Override
    public void limpiarEnsamblaje() {
        fachada.limpiarEnsamblaje();
        this.categoriaActual = null;
        this.marcaActual = null;
        this.componenteSeleccionado = null;
    }

    @Override
    public ComponenteDTO getComponenteSeleccionado(String categoria) {
        return fachada.getComponenteSeleccionado(categoria);
    }

    @Override
    public ComponenteDTO convertirProductoADTO(String productoId) {
        return fachada.convertirProductoADTO(productoId);
    }

    @Override
    public List<ComponenteDTO> obtenerProductosAleatorios(int cantidad) {
        return fachada.obtenerProductosAleatorios(cantidad);
    }
}
