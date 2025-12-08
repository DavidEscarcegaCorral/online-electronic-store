package objetosNegocio.mappers;

import entidades.PedidoEntidad;
import objetosNegocio.pedidoON.PedidoON;

public class PedidoMapper {
    public static PedidoON toON(PedidoEntidad entidad) {
        if (entidad == null) return null;
        return new PedidoON(
            entidad.getId() != null ? entidad.getId().toString() : null,
            entidad.getClienteId(),
            entidad.getTotal(),
            entidad.getEstado()
        );
    }

    public static PedidoEntidad toEntidad(PedidoON on) {
        if (on == null) return null;
        PedidoEntidad entidad = new PedidoEntidad();
        entidad.setClienteId(on.getClienteId());
        entidad.setTotal(on.getTotal());
        entidad.setEstado(on.getEstado());
        return entidad;
    }
}

