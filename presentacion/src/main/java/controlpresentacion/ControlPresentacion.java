package controlpresentacion;

import dto.ComponenteDTO;
import dto.EnsamblajeDTO;
import fachada.ConfiguracionFacade;
import fachada.IArmadoFacade;
import fachada.ArmadoFacade;

import java.util.List;

public class ControlPresentacion {

    private final ConfiguracionFacade configuracionFacade;
    private final IArmadoFacade armadoFacade;

    private String categoriaActual;
    private String marcaActual;
    private ComponenteDTO componenteSeleccionado;

    private static ControlPresentacion instancia;

    private ControlPresentacion() {
        this.configuracionFacade = ConfiguracionFacade.getInstance();
        this.armadoFacade = ArmadoFacade.getInstance();
    }

    public static synchronized ControlPresentacion getInstance() {
        if (instancia == null) {
            instancia = new ControlPresentacion();
        }
        return instancia;
    }

    public List<String> obtenerCategorias() {
        return configuracionFacade.obtenerCategorias();
    }

    public void seleccionarCategoria(String categoria) {
        this.categoriaActual = categoria;
        this.marcaActual = null;
        this.componenteSeleccionado = null;

        dto.ComponenteDTO seleccionado = armadoFacade.getComponenteSeleccionado(categoria);
        if (seleccionado != null) {
            this.marcaActual = seleccionado.getMarca();
            this.componenteSeleccionado = seleccionado;
        }
    }

    public List<String> obtenerMarcasParaCategoriaActual() {
        if (categoriaActual == null) {
            throw new IllegalStateException("Debe seleccionar una categoría primero");
        }
        return configuracionFacade.obtenerMarcasPorCategoria(categoriaActual);
    }

    public void seleccionarMarca(String marca) {
        this.marcaActual = marca;
        this.componenteSeleccionado = null;
    }

    public boolean hayProductosDisponibles() {
        if (categoriaActual == null || marcaActual == null) {
            return false;
        }
        return configuracionFacade.hayProductosDisponibles(categoriaActual, marcaActual);
    }

    public List<ComponenteDTO> obtenerProductos() {
        if (categoriaActual == null || marcaActual == null) {
            throw new IllegalStateException("Debe seleccionar categoría y marca primero");
        }
        return configuracionFacade.obtenerProductosPorCategoriaYMarca(categoriaActual, marcaActual);
    }

    public void seleccionarProducto(ComponenteDTO producto) {
        this.componenteSeleccionado = producto;
    }

    public boolean puedeAvanzar() {
        return componenteSeleccionado != null;
    }

    public List<String> avanzarConComponenteSeleccionado() {
        if (componenteSeleccionado == null) {
            throw new IllegalStateException("No hay componente seleccionado");
        }

        List<String> errores = armadoFacade.agregarComponente(componenteSeleccionado);

        if (errores.isEmpty()) {
            this.categoriaActual = null;
            this.marcaActual = null;
            this.componenteSeleccionado = null;
        }

        return errores;
    }

    public boolean puedeVolverAtras(String categoria) {
        return armadoFacade.puedeVolverAtras(categoria);
    }

    public void cambiarComponente(String categoria) {
        armadoFacade.removerComponente(categoria);
        this.categoriaActual = categoria;
        this.marcaActual = null;
        this.componenteSeleccionado = null;
    }

    public List<String> revalidarEnsamblaje() {
        return armadoFacade.revalidarEnsamblaje();
    }

    public EnsamblajeDTO getEnsamblajeActual() {
        return armadoFacade.getEnsamblajeActual();
    }

    public void iniciarNuevoEnsamblaje() {
        armadoFacade.iniciarNuevoEnsamblaje();
        this.categoriaActual = null;
        this.marcaActual = null;
        this.componenteSeleccionado = null;
    }

    public String getCategoriaActual() {
        return categoriaActual;
    }

    public String getMarcaActual() {
        return marcaActual;
    }

    public ComponenteDTO getComponenteSeleccionado() {
        return componenteSeleccionado;
    }
}

