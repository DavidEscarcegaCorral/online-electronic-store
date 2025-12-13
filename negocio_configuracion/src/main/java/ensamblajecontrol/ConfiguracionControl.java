package ensamblajecontrol;

import dao.ConfiguracionDAO;
import dao.IProductoDAO;
import dao.ProductoDAO;
import dto.ComponenteDTO;
import dto.EnsamblajeDTO;
import entidades.ConfiguracionEntidad;
import entidades.ProductoEntidad;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfiguracionControl implements IConfiguracionControl {
    private final ConfiguracionDAO configuracionDAO;
    private final IProductoDAO productoDAO;

    public ConfiguracionControl() {
        this.configuracionDAO = new ConfiguracionDAO();
        this.productoDAO = new ProductoDAO();
    }


    @Override
    public String guardarConfiguracion(EnsamblajeDTO ensamblaje, String usuarioId) {
        if (ensamblaje == null || ensamblaje.obtenerTodosComponentes().isEmpty()) {
            return null;
        }

        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            return null;
        }

        ConfiguracionEntidad cfg = convertirAEntidad(ensamblaje, usuarioId);
        try {
            configuracionDAO.guardar(cfg);
            return cfg.getId().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    @Override
    public boolean tieneMinimoPorCategoria(String categoria, int minimo) {
        if (categoria == null || minimo <= 0) return false;
        long disponibles = productoDAO.contarDisponiblesPorCategoria(categoria);
        return disponibles >= minimo;
    }

    @Override
    public boolean tieneMarcaEnCategoria(String categoria, String marca) {
        if (categoria == null || marca == null) return false;
        List<ProductoEntidad> productos = productoDAO.obtenerPorCategoriaYMarca(categoria, marca);
        return !productos.isEmpty();
    }

    private ConfiguracionEntidad convertirAEntidad(EnsamblajeDTO ensamblajeDTO, String usuarioId) {
        ConfiguracionEntidad entidad = new ConfiguracionEntidad();
        entidad.setNombre("Configuración " + LocalDateTime.now());
        entidad.setUsuarioId(usuarioId);

        List<Map<String, Object>> componentesList = new ArrayList<>();
        for (ComponenteDTO comp : ensamblajeDTO.obtenerTodosComponentes()) {
            Map<String, Object> compMap = new HashMap<>();
            compMap.put("categoria", comp.getCategoria());
            compMap.put("id", comp.getId());
            compMap.put("nombre", comp.getNombre());
            compMap.put("precio", comp.getPrecio());
            compMap.put("marca", comp.getMarca());
            componentesList.add(compMap);
        }

        entidad.setComponentes(componentesList);
        entidad.setPrecioTotal(ensamblajeDTO.getPrecioTotal());

        return entidad;
    }

    @Override
    public ComponenteDTO convertirProductoADTO(String productoId) {
        if (productoId == null || productoId.trim().isEmpty()) {
            return null;
        }

        try {
            ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
            if (producto == null) {
                return null;
            }
            return convertirADTO(producto);
        } catch (Exception e) {
            System.err.println("Error al convertir producto a DTO: " + e.getMessage());
            return null;
        }
    }

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

