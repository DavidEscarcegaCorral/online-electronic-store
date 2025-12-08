package objetosNegocio.mappers;

import entidades.UsuarioEntidad;
import objetosNegocio.usuarioON.UsuarioON;

public class UsuarioMapper {
    public static UsuarioON toON(UsuarioEntidad entidad) {
        if (entidad == null) return null;
        return new UsuarioON(
            entidad.getId() != null ? entidad.getId().toString() : null,
            entidad.getNombre(),
            entidad.getEmail()
        );
    }

    public static UsuarioEntidad toEntidad(UsuarioON on) {
        if (on == null) return null;
        UsuarioEntidad entidad = new UsuarioEntidad();
        entidad.setNombre(on.getNombre());
        entidad.setEmail(on.getEmail());
        return entidad;
    }
}

