package ensamblajecontrol;

import dto.ComponenteDTO;
import dto.EnsamblajeDTO;
import java.util.List;

public interface IArmadoControl {
    String guardarConfiguracion(EnsamblajeDTO ensamblaje, String usuarioId);

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
}

