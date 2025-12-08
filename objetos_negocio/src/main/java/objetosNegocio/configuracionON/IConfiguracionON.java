package objetosNegocio.configuracionON;

import java.util.List;
import java.util.Map;

public interface IConfiguracionON {
    String getId();
    String getNombre();
    List<Map<String, Object>> getComponentes();
    Double getPrecioTotal();
}

