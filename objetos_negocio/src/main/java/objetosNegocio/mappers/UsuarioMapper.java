package objetosNegocio.mappers;

import entidades.UsuarioEntidad;
import objetosNegocio.UsuarioBO;

/**
 * Mapper para convertir entre UsuarioEntidad y UsuarioBO.
 */
public class UsuarioMapper {

    /**
     * Convierte una UsuarioEntidad a UsuarioBO.
     */
    public static UsuarioBO entidadABO(UsuarioEntidad entidad) {
        if (entidad == null) return null;
        return new UsuarioBO(
            entidad.getId() != null ? entidad.getId().toString() : null,
            entidad.getNombre(),
            entidad.getEmail()
        );
    }

    /**
     * Convierte un UsuarioBO a UsuarioEntidad.
     */
    public static UsuarioEntidad boAEntidad(UsuarioBO bo) {
        if (bo == null) return null;
        UsuarioEntidad entidad = new UsuarioEntidad();
        entidad.setNombre(bo.getNombre());
        entidad.setEmail(bo.getEmail());
        return entidad;
    }
}

