package objetosNegocio.mappers;

import dto.ConfiguracionDTO;
import entidades.ConfiguracionEntidad;
import objetosNegocio.ConfiguracionBO;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper unificado para ConfiguraciónEntidada a ConfiguracionBO a ConfiguracionDTO.
 */
public class ConfiguracionMapper {

    /**
     * Convierte una ConfiguracionEntidad a ConfiguracionBO.
     */
    public static ConfiguracionBO entidadABO(ConfiguracionEntidad entidad) {
        if (entidad == null) {
            return null;
        }

        ConfiguracionBO bo = new ConfiguracionBO();
        bo.setId(entidad.getId() != null ? entidad.getId().toString() : null);
        bo.setUsuarioId(entidad.getUsuarioId());
        bo.setNombre(entidad.getNombre());
        bo.setComponentes(entidad.getComponentes());
        bo.setPrecioTotal(entidad.getPrecioTotal());
        bo.setFechaCreacion(entidad.getFechaCreacion());

        return bo;
    }

    /**
     * Convierte un ConfiguracionBO a ConfiguracionEntidad.
     */
    public static ConfiguracionEntidad boAEntidad(ConfiguracionBO bo) {
        if (bo == null) {
            return null;
        }

        ConfiguracionEntidad entidad = new ConfiguracionEntidad();
        if (bo.getId() != null) {
            entidad.setId(new ObjectId(bo.getId()));
        }
        entidad.setUsuarioId(bo.getUsuarioId());
        entidad.setNombre(bo.getNombre());
        entidad.setComponentes(bo.getComponentes());
        entidad.setPrecioTotal(bo.getPrecioTotal());
        entidad.setFechaCreacion(bo.getFechaCreacion());

        return entidad;
    }

    /**
     * Convierte un ConfiguracionBO a ConfiguracionDTO.
     *
     * @param bo El objeto de negocio con BigDecimal
     * @return El DTO para la capa de presentación con double
     */
    public static ConfiguracionDTO boADTO(ConfiguracionBO bo) {
        if (bo == null) {
            return null;
        }

        ConfiguracionDTO dto = new ConfiguracionDTO();
        dto.setId(bo.getId());
        dto.setUsuarioId(bo.getUsuarioId());
        dto.setNombre(bo.getNombre());

        dto.setPrecioTotal(bo.getPrecioTotal() != null ? bo.getPrecioTotal().doubleValue() : 0.0);

        if (bo.getComponentes() != null) {
            dto.setComponentes(new ArrayList<>(bo.getComponentes()));
        }

        return dto;
    }

    /**
     * Convierte un ConfiguracionDTO a ConfiguracionBO.
     *
     * @param dto El DTO de la capa de presentación con double
     * @return El objeto de negocio con BigDecimal
     */
    public static ConfiguracionBO dtoABO(ConfiguracionDTO dto) {
        if (dto == null) {
            return null;
        }

        ConfiguracionBO bo = new ConfiguracionBO();
        bo.setId(dto.getId());
        bo.setUsuarioId(dto.getUsuarioId());
        bo.setNombre(dto.getNombre());

        bo.setPrecioTotal(BigDecimal.valueOf(dto.getPrecioTotal()));

        if (dto.getComponentes() != null) {
            bo.setComponentes(new ArrayList<>(dto.getComponentes()));
        }

        return bo;
    }

    /**
     * Convierte una lista de ConfiguracionBO a lista de ConfiguracionDTO.
     */
    public static List<ConfiguracionDTO> listaBOADTO(List<ConfiguracionBO> bos) {
        if (bos == null) {
            return new ArrayList<>();
        }

        List<ConfiguracionDTO> dtos = new ArrayList<>();
        for (ConfiguracionBO bo : bos) {
            ConfiguracionDTO dto = boADTO(bo);
            if (dto != null) {
                dtos.add(dto);
            }
        }
        return dtos;
    }
}

