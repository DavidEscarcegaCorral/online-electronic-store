package objetosNegocio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Interfaz para el Business Object de Configuración de PC.
 * Define los métodos públicos para la lógica de negocio de configuraciones.
 */
public interface IConfiguracionBO {

    String getId();

    void setId(String id);

    String getUsuarioId();

    void setUsuarioId(String usuarioId);

    String getNombre();

    void setNombre(String nombre);

    List<Map<String, Object>> getComponentes();

    void setComponentes(List<Map<String, Object>> componentes);

    BigDecimal getPrecioTotal();

    void setPrecioTotal(BigDecimal precioTotal);

    LocalDateTime getFechaCreacion();

    void setFechaCreacion(LocalDateTime fechaCreacion);

    boolean esValida();

    int getCantidadComponentes();
}

