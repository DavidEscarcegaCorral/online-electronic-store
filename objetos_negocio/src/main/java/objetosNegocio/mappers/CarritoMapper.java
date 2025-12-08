package objetosNegocio.mappers;

import entidades.CarritoEntidad;
import objetosNegocio.carritoON.CarritoON;

public class CarritoMapper {
    public static CarritoON toON(CarritoEntidad entidad) {
        if (entidad == null) return null;
        return new CarritoON(
            entidad.getId() != null ? entidad.getId().toString() : null,
            entidad.getClienteId(),
            entidad.getConfiguracionesIds() != null ? entidad.getConfiguracionesIds().stream().map(o -> (Object) o.toString()).toList() : null
        );
    }

    public static CarritoEntidad toEntidad(CarritoON on) {
        if (on == null) return null;
        CarritoEntidad entidad = new CarritoEntidad();
        entidad.setClienteId(on.getClienteId());
        return entidad;
    }
}

