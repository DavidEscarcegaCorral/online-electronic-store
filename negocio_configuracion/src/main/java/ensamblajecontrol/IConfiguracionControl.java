package ensamblajecontrol;

import dto.ComponenteDTO;
import dto.EnsamblajeDTO;
import java.util.List;

public interface IConfiguracionControl {
    String guardarConfiguracion(EnsamblajeDTO ensamblaje, String usuarioId);

    List<String> obtenerCategorias();

    List<String> obtenerMarcasPorCategoria(String categoria);

    List<ComponenteDTO> obtenerProductosPorCategoriaYMarca(String categoria, String marca);

    boolean hayProductosDisponibles(String categoria, String marca);

    boolean tieneMinimoPorCategoria(String categoria, int minimo);

    boolean tieneMarcaEnCategoria(String categoria, String marca);

    ComponenteDTO convertirProductoADTO(String productoId);
}

