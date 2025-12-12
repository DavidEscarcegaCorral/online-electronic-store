package controlconfig;

import dto.ComponenteDTO;
import dto.EnsamblajeDTO;
import java.util.List;

public interface IFachadaControl {
    // Métodos de ArmadoControl
    String guardarConfiguracion(EnsamblajeDTO ensamblaje);
    EnsamblajeDTO iniciarNuevoEnsamblaje();
    EnsamblajeDTO getEnsamblajeActual();
    List<ComponenteDTO> obtenerComponentesPorCategoria(String categoria);
    List<String> agregarComponente(ComponenteDTO componente);
    boolean verificarStockSuficiente(String tipoUso);
    List<ComponenteDTO> obtenerComponentesCompatibles(String categoria, String tipoUso);
    boolean puedeVolverAtras(String categoria);
    void removerComponente(String categoria);
    List<String> revalidarEnsamblaje();
    ComponenteDTO getComponenteSeleccionado(String categoria);
    void limpiarEnsamblaje();
    void removerComponentesPosteriores(String categoria);

    // Métodos de ConfiguracionControl
    List<String> obtenerCategorias();
    List<String> obtenerMarcasPorCategoria(String categoria);
    List<ComponenteDTO> obtenerProductosPorCategoriaYMarca(String categoria, String marca);
    boolean hayProductosDisponibles(String categoria, String marca);
    boolean tieneMinimoPorCategoria(String categoria, int minimo);
    boolean tieneMarcaEnCategoria(String categoria, String marca);
}

