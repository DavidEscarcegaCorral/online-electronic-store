package objetosNegocio.mappers;

import dto.ConfiguracionDTO;
import dto.EnsamblajeDTO;
import dto.ComponenteDTO;
import entidades.ConfiguracionEntidad;
import objetosNegocio.ConfiguracionBO;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
     * Convierte un EnsamblajeDTO a ConfiguracionBO.
     *
     * @param ensamblaje El ensamblaje (configuración temporal) del usuario
     * @param usuarioId El ID del usuario propietario
     * @param nombre El nombre de la configuración
     * @return ConfiguracionBO listo para ser convertido a Entidad
     */
    public static ConfiguracionBO ensamblajeABO(EnsamblajeDTO ensamblaje, String usuarioId, String nombre) {
        if (ensamblaje == null) {
            return null;
        }

        ConfiguracionBO bo = new ConfiguracionBO();
        bo.setUsuarioId(usuarioId);
        bo.setNombre(nombre != null ? nombre : "Configuración " + LocalDateTime.now());
        bo.setFechaCreacion(LocalDateTime.now());

        List<Map<String, Object>> componentesList = new ArrayList<>();
        for (ComponenteDTO comp : ensamblaje.obtenerTodosComponentes()) {
            Map<String, Object> compMap = new HashMap<>();
            compMap.put("categoria", comp.getCategoria());
            compMap.put("id", comp.getId());
            compMap.put("nombre", comp.getNombre());
            compMap.put("precio", comp.getPrecio());
            compMap.put("marca", comp.getMarca());
            componentesList.add(compMap);
        }
        bo.setComponentes(componentesList);
        bo.setPrecioTotal(BigDecimal.valueOf(ensamblaje.getPrecioTotal()));

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

