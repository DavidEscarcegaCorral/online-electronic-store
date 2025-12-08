package objetosNegocio.mappers;

import dto.ComponenteDTO;
import entidades.ProductoEntidad;

public class ComponenteMapper {
    public static ComponenteDTO toDTO(ProductoEntidad entidad) {
        if (entidad == null) return null;
        ComponenteDTO componente = new ComponenteDTO();
        componente.setId(entidad.getId() != null ? entidad.getId().toString() : null);
        componente.setNombre(entidad.getNombre());
        componente.setPrecio(entidad.getPrecio());
        componente.setCategoria(entidad.getCategoria());
        componente.setMarca(entidad.getMarca());
        componente.setDescripcion(entidad.getDescripcion());
        componente.setStock(entidad.getStock());
        componente.setImagenUrl(entidad.getImagenUrl());
        if (entidad.getEspecificaciones() != null) {
            componente.setSocket(entidad.getEspecificaciones().get("socket"));
            componente.setTipoRam(entidad.getEspecificaciones().get("tipoRam"));
            componente.setFormFactor(entidad.getEspecificaciones().get("formFactor"));
        }
        return componente;
    }

    public static ProductoEntidad toEntidad(ComponenteDTO dto) {
        if (dto == null) return null;
        ProductoEntidad entidad = new ProductoEntidad();
        entidad.setNombre(dto.getNombre());
        entidad.setPrecio(dto.getPrecio());
        entidad.setCategoria(dto.getCategoria());
        entidad.setMarca(dto.getMarca());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setStock(dto.getStock());
        entidad.setImagenUrl(dto.getImagenUrl());
        return entidad;
    }
}

