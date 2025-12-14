package objetosNegocio;

import dao.ProductoDAO;
import dto.ComponenteDTO;
import entidades.ProductoEntidad;
import objetosNegocio.mappers.ProductoMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Business Object para gestión de componentes.
 */
public class ComponenteBO implements IComponenteBO {

    private static ComponenteBO componenteON;
    private final ProductoDAO productoDAO;

    public static synchronized IComponenteBO getInstance() {
        if (componenteON == null) {
            componenteON = new ComponenteBO();
        }
        return componenteON;
    }

    private ComponenteBO() {
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

    /**
     * Convierte ProductoEntidad a ComponenteDTO usando ProductoMapper.
     */
    private ComponenteDTO convertirADTO(ProductoEntidad entidad) {
        if (entidad == null) {
            return null;
        }

        // Usar ProductoMapper para hacer la conversión
        ProductoBO productoBO = ProductoMapper.entidadABO(entidad);
        return ProductoMapper.boADTO(productoBO);
    }
}
