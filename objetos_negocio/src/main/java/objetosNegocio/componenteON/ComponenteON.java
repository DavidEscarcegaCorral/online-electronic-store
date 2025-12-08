package objetosNegocio.componenteON;

import dao.ProductoDAO;
import dto.ComponenteDTO;
import entidades.ProductoEntidad;

import java.util.List;
import java.util.stream.Collectors;

public class ComponenteON implements IComponenteON {

    private static ComponenteON componenteON;
    private final ProductoDAO productoDAO;

    public static synchronized ComponenteON getInstance() {
        if (componenteON == null) {
            componenteON = new ComponenteON();
        }
        return componenteON;
    }

    private ComponenteON() {
        this.productoDAO = new ProductoDAO();
    }

    @Override
    public List<ComponenteDTO> obtenerTodos() {
        try {
            List<ProductoEntidad> productos = productoDAO.obtenerTodos();
            return productos.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener componentes: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ComponenteDTO> obtenerPorCategoria(String categoria) {
        try {
            List<ProductoEntidad> productos = productoDAO.obtenerPorCategoria(categoria);
            return productos.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener componentes por categoría: " + e.getMessage(), e);
        }
    }

    @Override
    public ComponenteDTO obtenerPorId(String id) {
        try {
            ProductoEntidad producto = productoDAO.obtenerPorId(id);
            return producto != null ? convertirADTO(producto) : null;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener componente por ID: " + e.getMessage(), e);
        }
    }

    private ComponenteDTO convertirADTO(ProductoEntidad producto) {
        ComponenteDTO dto = new ComponenteDTO();
        dto.setId(producto.getId().toString());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setCategoria(producto.getCategoria());
        dto.setMarca(producto.getMarca());
        dto.setDescripcion(producto.getDescripcion());
        dto.setStock(producto.getStock());
        dto.setImagenUrl(producto.getImagenUrl());

        if (producto.getEspecificaciones() != null) {
            dto.setSocket(producto.getEspecificaciones().get("socket"));
            dto.setTipoRam(producto.getEspecificaciones().get("tipoRam"));
            dto.setFormFactor(producto.getEspecificaciones().get("formFactor"));
        }

        return dto;
    }
}
