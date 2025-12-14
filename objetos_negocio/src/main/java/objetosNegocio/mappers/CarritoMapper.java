package objetosNegocio.mappers;

import dto.CarritoDTO;
import dto.ItemCarritoDTO;
import entidades.CarritoEntidad;
import objetosNegocio.CarritoBO;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper para convertir entre CarritoEntidad y CarritoBO.
 */
public class CarritoMapper {

    /**
     * Convierte una CarritoEntidad a CarritoBO.
     *
     * @param entidad La entidad de base de datos con ObjectId y estructura de MongoDB
     * @return El objeto de negocio con tipos Java estándar
     */
    public static CarritoBO entidadABO(CarritoEntidad entidad) {
        if (entidad == null) {
            return null;
        }

        CarritoBO bo = new CarritoBO();

        bo.setId(entidad.getId() != null ? entidad.getId().toString() : null);
        bo.setClienteId(entidad.getClienteId());

        if (entidad.getProductos() != null) {
            List<CarritoBO.ItemCarritoBO> itemsBo = new ArrayList<>();
            for (Map<String, Object> productoMap : entidad.getProductos()) {
                itemsBo.add(mapAItemCarritoBO(productoMap));
            }
            bo.setProductos(itemsBo);
        }

        if (entidad.getConfiguracionesIds() != null) {
            List<String> configuracionesIdsString = new ArrayList<>();
            for (ObjectId objectId : entidad.getConfiguracionesIds()) {
                configuracionesIdsString.add(objectId.toString());
            }
            bo.setConfiguracionesIds(configuracionesIdsString);
        }

        return bo;
    }

    /**
     * Convierte un CarritoBO a CarritoEntidad.
     *
     * @param bo El objeto de negocio con tipos Java estándar
     * @return La entidad de base de datos con ObjectId y estructura MongoDB
     */
    public static CarritoEntidad boAEntidad(CarritoBO bo) {
        if (bo == null) {
            return null;
        }

        CarritoEntidad entidad = new CarritoEntidad();

        if (bo.getId() != null) {
            entidad.setId(new ObjectId(bo.getId()));
        }

        entidad.setClienteId(bo.getClienteId());

        if (bo.getProductos() != null) {
            List<Map<String, Object>> productosMap = new ArrayList<>();
            for (CarritoBO.ItemCarritoBO itemBo : bo.getProductos()) {
                productosMap.add(itemCarritoBOAMap(itemBo));
            }
            entidad.setProductos(productosMap);
        }

        if (bo.getConfiguracionesIds() != null) {
            List<ObjectId> configuracionesIdsObjectId = new ArrayList<>();
            for (String idString : bo.getConfiguracionesIds()) {
                configuracionesIdsObjectId.add(new ObjectId(idString));
            }
            entidad.setConfiguracionesIds(configuracionesIdsObjectId);
        }

        return entidad;
    }


    /**
     * Convierte un CarritoBO a CarritoDTO.
     */
    public static CarritoDTO boADTO(CarritoBO bo) {
        if (bo == null) {
            return null;
        }

        CarritoDTO dto = new CarritoDTO();
        dto.setClienteId(bo.getClienteId());

        if (bo.getProductos() != null) {
            List<ItemCarritoDTO> items = new ArrayList<>();
            for (CarritoBO.ItemCarritoBO itemBo : bo.getProductos()) {
                items.add(itemCarritoBOADTO(itemBo));
            }
            dto.setItems(items);
        }

        return dto;
    }

    /**
     * Convierte un CarritoDTO a CarritoBO.
     */
    public static CarritoBO dtoABO(CarritoDTO dto) {
        if (dto == null) {
            return null;
        }

        CarritoBO bo = new CarritoBO();
        bo.setClienteId(dto.getClienteId());

        if (dto.getItems() != null) {
            List<CarritoBO.ItemCarritoBO> productos = new ArrayList<>();
            for (ItemCarritoDTO itemDto : dto.getItems()) {
                productos.add(itemCarritoDTOABO(itemDto));
            }
            bo.setProductos(productos);
        }

        return bo;
    }

    private static ItemCarritoDTO itemCarritoBOADTO(CarritoBO.ItemCarritoBO itemBo) {
        if (itemBo == null) return null;

        ItemCarritoDTO itemDto = new ItemCarritoDTO();
        itemDto.setProductoId(itemBo.getProductoId());
        itemDto.setNombre(itemBo.getNombre());
        itemDto.setCantidad(itemBo.getCantidad());
        itemDto.setPrecioUnitario(itemBo.getPrecio() != null ? itemBo.getPrecio().doubleValue() : 0.0);

        return itemDto;
    }

    private static CarritoBO.ItemCarritoBO itemCarritoDTOABO(ItemCarritoDTO itemDto) {
        if (itemDto == null) return null;

        CarritoBO.ItemCarritoBO itemBo = new CarritoBO.ItemCarritoBO();
        itemBo.setProductoId(itemDto.getProductoId());
        itemBo.setNombre(itemDto.getNombre());
        itemBo.setCantidad(itemDto.getCantidad());
        itemBo.setPrecio(BigDecimal.valueOf(itemDto.getPrecioUnitario()));

        return itemBo;
    }

    /**
     * Convierte Map<String, Object> a ItemCarritoBO.
     */
    private static CarritoBO.ItemCarritoBO mapAItemCarritoBO(Map<String, Object> productoMap) {
        if (productoMap == null) return null;

        CarritoBO.ItemCarritoBO itemBo = new CarritoBO.ItemCarritoBO();

        Object productoIdObj = productoMap.get("productoId");
        if (productoIdObj != null) {
            itemBo.setProductoId(productoIdObj.toString());
        }

        Object nombreObj = productoMap.get("nombre");
        if (nombreObj != null) {
            itemBo.setNombre(nombreObj.toString());
        }

        Object cantidadObj = productoMap.get("cantidad");
        if (cantidadObj instanceof Integer) {
            itemBo.setCantidad((Integer) cantidadObj);
        }

        Object precioObj = productoMap.get("precio");
        if (precioObj instanceof Double) {
            itemBo.setPrecio(BigDecimal.valueOf((Double) precioObj));
        } else if (precioObj instanceof Number) {
            itemBo.setPrecio(BigDecimal.valueOf(((Number) precioObj).doubleValue()));
        }

        return itemBo;
    }

    /**
     * Convierte ItemCarritoBO a Map<String, Object>.
     */
    private static Map<String, Object> itemCarritoBOAMap(CarritoBO.ItemCarritoBO itemBo) {
        if (itemBo == null) return null;

        Map<String, Object> map = new HashMap<>();
        map.put("productoId", itemBo.getProductoId());
        map.put("nombre", itemBo.getNombre());
        map.put("cantidad", itemBo.getCantidad());
        map.put("precio", itemBo.getPrecio() != null ? itemBo.getPrecio().doubleValue() : 0.0);

        return map;
    }
}
