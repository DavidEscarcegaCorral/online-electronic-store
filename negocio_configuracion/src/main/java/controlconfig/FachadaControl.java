package controlconfig;

import dto.EnsamblajeDTO;
import dto.ComponenteDTO;
import java.util.List;

public class FachadaControl implements IFachadaControl {
    private static FachadaControl instancia;
    private final IArmadoControl armadoControl;
    private final IConfiguracionControl configuracionControl;

    private FachadaControl() {
        this.armadoControl = ArmadoControl.getInstance();
        this.configuracionControl = ConfiguracionControl.getInstance();
    }

    public static synchronized FachadaControl getInstance() {
        if (instancia == null) {
            instancia = new FachadaControl();
        }
        return instancia;
    }

    @Override
    public String guardarConfiguracion(EnsamblajeDTO ensamblaje) {
        return armadoControl.guardarConfiguracion(ensamblaje);
    }

    @Override
    public EnsamblajeDTO iniciarNuevoEnsamblaje() {
        return armadoControl.iniciarNuevoEnsamblaje();
    }

    @Override
    public EnsamblajeDTO getEnsamblajeActual() {
        return armadoControl.getEnsamblajeActual();
    }

    @Override
    public List<ComponenteDTO> obtenerComponentesPorCategoria(String categoria) {
        return armadoControl.obtenerComponentesPorCategoria(categoria);
    }

    @Override
    public List<String> agregarComponente(ComponenteDTO componente) {
        return armadoControl.agregarComponente(componente);
    }

    @Override
    public boolean verificarStockSuficiente(String tipoUso) {
        return armadoControl.verificarStockSuficiente(tipoUso);
    }

    @Override
    public List<ComponenteDTO> obtenerComponentesCompatibles(String categoria, String tipoUso) {
        return armadoControl.obtenerComponentesCompatibles(categoria, tipoUso);
    }

    @Override
    public boolean puedeVolverAtras(String categoria) {
        return armadoControl.puedeVolverAtras(categoria);
    }

    @Override
    public void removerComponente(String categoria) {
        armadoControl.removerComponente(categoria);
    }

    @Override
    public List<String> revalidarEnsamblaje() {
        return armadoControl.revalidarEnsamblaje();
    }

    @Override
    public ComponenteDTO getComponenteSeleccionado(String categoria) {
        return armadoControl.getComponenteSeleccionado(categoria);
    }

    @Override
    public void limpiarEnsamblaje() {
        armadoControl.limpiarEnsamblaje();
    }

    @Override
    public void removerComponentesPosteriores(String categoria) {
        armadoControl.removerComponentesPosteriores(categoria);
    }

    @Override
    public boolean tieneMinimoPorCategoria(String categoria, int minimo) {
        return configuracionControl.tieneMinimoPorCategoria(categoria, minimo);
    }

    @Override
    public boolean tieneMarcaEnCategoria(String categoria, String marca) {
        return configuracionControl.tieneMarcaEnCategoria(categoria, marca);
    }

    @Override
    public List<String> obtenerCategorias() {
        return configuracionControl.obtenerCategorias();
    }

    @Override
    public List<String> obtenerMarcasPorCategoria(String categoria) {
        return configuracionControl.obtenerMarcasPorCategoria(categoria);
    }

    @Override
    public List<ComponenteDTO> obtenerProductosPorCategoriaYMarca(String categoria, String marca) {
        return configuracionControl.obtenerProductosPorCategoriaYMarca(categoria, marca);
    }

    @Override
    public boolean hayProductosDisponibles(String categoria, String marca) {
        return configuracionControl.hayProductosDisponibles(categoria, marca);
    }
}

