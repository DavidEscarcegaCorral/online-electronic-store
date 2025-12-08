package objetosNegocio.mappers;

import entidades.ConfiguracionEntidad;
import objetosNegocio.configuracionON.ConfiguracionON;

public class ConfiguracionMapper {
    public static ConfiguracionON toON(ConfiguracionEntidad entidad) {
        if (entidad == null) return null;
        return new ConfiguracionON(
            entidad.getId() != null ? entidad.getId().toString() : null,
            entidad.getNombre(),
            entidad.getComponentes(),
            entidad.getPrecioTotal()
        );
    }

    public static ConfiguracionEntidad toEntidad(ConfiguracionON on) {
        if (on == null) return null;
        ConfiguracionEntidad entidad = new ConfiguracionEntidad();
        entidad.setNombre(on.getNombre());
        entidad.setComponentes(on.getComponentes());
        entidad.setPrecioTotal(on.getPrecioTotal());
        return entidad;
    }
}

