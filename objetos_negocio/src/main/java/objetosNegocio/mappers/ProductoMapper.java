package objetosNegocio.mappers;

import dto.ComponenteDTO;
import entidades.ProductoEntidad;
import objetosNegocio.ProductoBO;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Mapper unificado para ProductoEntidad ↔ ProductoBO ↔ ComponenteDTO.
 *
 */
public class ProductoMapper {

    /**
     * Convierte una ProductoEntidad a ProductoBO.
     *
     * @param entidad La entidad de base de datos
     * @return El objeto de negocio
     */
    public static ProductoBO entidadABO(ProductoEntidad entidad) {
        if (entidad == null) {
            return null;
        }

        ProductoBO bo = new ProductoBO();
        bo.setId(entidad.getId() != null ? entidad.getId().toString() : null);
        bo.setNombre(entidad.getNombre());
        bo.setCategoria(entidad.getCategoria());
        bo.setMarca(entidad.getMarca());
        bo.setPrecio(entidad.getPrecio());
        bo.setStock(entidad.getStock());
        bo.setEspecificaciones(entidad.getEspecificaciones());
        bo.setDescripcion(entidad.getDescripcion());
        bo.setImagenUrl(entidad.getImagenUrl());

        return bo;
    }

    /**
     * Convierte un ProductoBOa ProductoEntidad.
     *
     * @param bo El objeto de negocio
     * @return La entidad de base de datos
     */
    public static ProductoEntidad boAEntidad(ProductoBO bo) {
        if (bo == null) {
            return null;
        }

        ProductoEntidad entidad = new ProductoEntidad();
        if (bo.getId() != null) {
            entidad.setId(new ObjectId(bo.getId()));
        }
        entidad.setNombre(bo.getNombre());
        entidad.setCategoria(bo.getCategoria());
        entidad.setMarca(bo.getMarca());
        entidad.setPrecio(bo.getPrecio());
        entidad.setStock(bo.getStock());
        entidad.setEspecificaciones(bo.getEspecificaciones());
        entidad.setDescripcion(bo.getDescripcion());
        entidad.setImagenUrl(bo.getImagenUrl());

        return entidad;
    }

    /**
     * Convierte un ProductoBOa ComponenteDTO.
     */
    public static dto.ComponenteDTO boADTO(ProductoBO bo) {
        if (bo == null) {
            return null;
        }

        dto.ComponenteDTO dto = new ComponenteDTO();
        dto.setId(bo.getId());
        dto.setNombre(bo.getNombre());
        dto.setCategoria(bo.getCategoria());
        dto.setMarca(bo.getMarca());
        dto.setPrecio(bo.getPrecio() != null ? bo.getPrecio().doubleValue() : 0.0);
        dto.setStock(bo.getStock());
        dto.setDescripcion(bo.getDescripcion());
        dto.setImagenUrl(bo.getImagenUrl());

        if (bo.getEspecificaciones() != null) {
            dto.setSocket(bo.getEspecificaciones().get("socket"));
            dto.setTipoRam(bo.getEspecificaciones().get("tipoRam"));
            dto.setFormFactor(bo.getEspecificaciones().get("formFactor"));

            String watts = bo.getEspecificaciones().get("watts");
            if (watts != null) {
                try {
                    dto.setWatts(Integer.parseInt(watts));
                } catch (NumberFormatException e) {
                    dto.setWatts(0);
                }
            }
        }

        return dto;
    }

    /**
     * Convierte un ComponenteDTO a ProductoBO.
     */
    public static ProductoBO dtoABO(ComponenteDTO dto) {
        if (dto == null) {
            return null;
        }

        ProductoBO bo = new ProductoBO();
        bo.setId(dto.getId());
        bo.setNombre(dto.getNombre());
        bo.setCategoria(dto.getCategoria());
        bo.setMarca(dto.getMarca());
        bo.setPrecio(BigDecimal.valueOf(dto.getPrecio()));
        bo.setStock(dto.getStock());
        bo.setDescripcion(dto.getDescripcion());
        bo.setImagenUrl(dto.getImagenUrl());

        Map<String, String> especificaciones = new HashMap<>();
        if (dto.getSocket() != null) {
            especificaciones.put("socket", dto.getSocket());
        }
        if (dto.getTipoRam() != null) {
            especificaciones.put("tipoRam", dto.getTipoRam());
        }
        if (dto.getFormFactor() != null) {
            especificaciones.put("formFactor", dto.getFormFactor());
        }
        if (dto.getWatts() > 0) {
            especificaciones.put("watts", String.valueOf(dto.getWatts()));
        }

        if (!especificaciones.isEmpty()) {
            bo.setEspecificaciones(especificaciones);
        }

        return bo;
    }
}

