package fachada;

import dao.IProductoDAO;
import dao.ProductoDAO;
import dto.ComponenteDTO;
import entidades.ProductoEntidad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Implementación de la fachada para el subsistema de Configuración.
 * Gestiona el flujo secuencial de selección de componentes.
 */
public class ConfiguracionFacade implements IConfiguracionFacade {
    private static ConfiguracionFacade instancia;
    private final IProductoDAO productoDAO;

    private ConfiguracionFacade() {
        this.productoDAO = new ProductoDAO();
    }

    public static synchronized ConfiguracionFacade getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionFacade();
        }
        return instancia;
    }

    @Override
    public List<String> obtenerCategorias() {
        return Arrays.asList(
                "Procesador",
                "Tarjeta Madre",
                "RAM",
                "Tarjeta de video",
                "Almacenamiento",
                "Fuente de poder",
                "Gabinete"
        );
    }

    @Override
    public List<String> obtenerMarcasPorCategoria(String categoria) {
        return productoDAO.obtenerMarcasPorCategoria(categoria);
    }

    @Override
    public List<ComponenteDTO> obtenerProductosPorCategoriaYMarca(String categoria, String marca) {
        List<ProductoEntidad> entidades = productoDAO.obtenerPorCategoriaYMarca(categoria, marca);
        List<ComponenteDTO> dtos = new ArrayList<>();

        for (ProductoEntidad entidad : entidades) {
            dtos.add(convertirADTO(entidad));
        }

        return dtos;
    }

    @Override
    public boolean hayProductosDisponibles(String categoria, String marca) {
        return !productoDAO.obtenerPorCategoriaYMarca(categoria, marca).isEmpty();
    }

    /**
     * Convierte una entidad de producto a DTO.
     */
    private ComponenteDTO convertirADTO(ProductoEntidad entidad) {
        ComponenteDTO dto = new ComponenteDTO();
        dto.setId(entidad.getId().toString());
        dto.setNombre(entidad.getNombre());
        dto.setCategoria(entidad.getCategoria());
        dto.setMarca(entidad.getMarca());
        dto.setPrecio(entidad.getPrecio());
        dto.setStock(entidad.getStock());
        dto.setDescripcion(entidad.getDescripcion());
        dto.setImagenUrl(entidad.getImagenUrl());

        Map<String, String> specs = entidad.getEspecificaciones();
        if (specs != null) {
            dto.setSocket(specs.get("socket"));
            dto.setTipoRam(specs.get("tipoRam"));
            dto.setFormFactor(specs.get("formFactor"));
            if (specs.containsKey("watts")) {
                try {
                    dto.setWatts(Integer.parseInt(specs.get("watts")));
                } catch (NumberFormatException e) {
                    dto.setWatts(0);
                }
            }
        }

        return dto;
    }
}

