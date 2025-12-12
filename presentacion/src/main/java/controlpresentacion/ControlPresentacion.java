package controlpresentacion;

import dto.ComponenteDTO;
import dto.EnsamblajeDTO;
import controlconfig.FachadaControl;

import java.util.List;

public class ControlPresentacion implements IControlPresentacion {

    private final FachadaControl fachada;

    private String categoriaActual;
    private String marcaActual;
    private ComponenteDTO componenteSeleccionado;

    private static ControlPresentacion instancia;

    private ControlPresentacion() {
        this.fachada = FachadaControl.getInstance();
    }

    public static synchronized ControlPresentacion getInstance() {
        if (instancia == null) {
            instancia = new ControlPresentacion();
        }
        return instancia;
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

        dto.ComponenteDTO seleccionado = fachada.getComponenteSeleccionado(categoria);
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
        return fachada.obtenerProductosPorCategoriaYMarca(categoriaActual, marcaActual);
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
}

