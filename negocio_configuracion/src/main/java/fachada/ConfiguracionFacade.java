package fachada;

import dao.IProductoDAO;
import dao.ProductoDAO;
import dto.ComponenteDTO;
import dto.EnsamblajeDTO;
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
    private static ConfiguracionFacade configInstancia;
    private final IProductoDAO productoDAO;
    private final IArmadoFacade armadoFacade;

    private ConfiguracionFacade() {
        this.productoDAO = new ProductoDAO();
        this.armadoFacade = ArmadoFacade.getInstance();
    }

    public static synchronized ConfiguracionFacade getInstance() {
        if (configInstancia == null) {
            configInstancia = new ConfiguracionFacade();
        }
        return configInstancia;
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
     * Aplica una configuración completa: crea un nuevo ensamblaje en el ArmadoFacade
     * y agrega cada componente presente en el EnsamblajeDTO. Se devuelven todos los
     * errores encontrados durante la inserción (compatibilidad, etc.).
     *
     * @param ensamblaje Ensamblaje con componentes por categoría.
     * @return Lista de errores encontrados; lista vacía si la configuración fue aplicada correctamente.
     */
    @Override
    public List<String> aplicarConfiguracion(EnsamblajeDTO ensamblaje) {
        List<String> errores = new ArrayList<>();

        if (ensamblaje == null) {
            errores.add("Ensamblaje nulo");
            return errores;
        }

        // Reiniciamos el ensamblaje
        armadoFacade.iniciarNuevoEnsamblaje();

        // Agregamos cada componente del ensamblaje
        for (ComponenteDTO componente : ensamblaje.obtenerTodosComponentes()) {
            if (componente == null || componente.getCategoria() == null) {
                errores.add("Componente o categoría nula en la configuración");
                continue;
            }

            List<String> erroresComponente = armadoFacade.agregarComponente(componente);
            for (String e : erroresComponente) {
                errores.add(componente.getCategoria() + ": " + e);
            }
        }

        // Revalidamos ensamblaje
        List<String> revalidacion = armadoFacade.revalidarEnsamblaje();
        errores.addAll(revalidacion);

        return errores;
    }

    @Override
    public boolean tieneMinimoPorCategoria(String categoria, int minimo) {
        if (categoria == null || minimo <= 0) return false;
        long disponibles = productoDAO.contarDisponiblesPorCategoria(categoria);
        return disponibles >= minimo;
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
