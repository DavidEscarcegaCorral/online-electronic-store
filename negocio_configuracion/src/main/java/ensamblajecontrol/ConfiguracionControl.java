package ensamblajecontrol;

import dao.ConfiguracionDAO;
import dao.ProductoDAO;
import dto.ComponenteDTO;
import dto.EnsamblajeDTO;
import entidades.ConfiguracionEntidad;
import entidades.ProductoEntidad;
import objetosNegocio.ConfiguracionBO;
import objetosNegocio.ProductoBO;
import objetosNegocio.mappers.ConfiguracionMapper;
import objetosNegocio.mappers.ProductoMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfiguracionControl implements IConfiguracionControl {
    private final ConfiguracionDAO configuracionDAO;
    private final ProductoDAO productoDAO;

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

        try {
            String nombreConfig = "Configuración " + LocalDateTime.now();
            ConfiguracionBO configuracionBO = ConfiguracionMapper.ensamblajeABO(ensamblaje, usuarioId, nombreConfig);

            ConfiguracionEntidad entidad = ConfiguracionMapper.boAEntidad(configuracionBO);

            configuracionDAO.guardar(entidad);

            return entidad.getId().toString();
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
    public List<ComponenteDTO> obtenerProductosPorCategoriaYMarca(String categoria, String marca, EnsamblajeDTO ensamblajeActual) {
        List<ProductoEntidad> entidades = productoDAO.obtenerPorCategoriaYMarca(categoria, marca);
        List<ComponenteDTO> dtos = new ArrayList<>();

        for (ProductoEntidad entidad : entidades) {
            ProductoBO bo = ProductoMapper.entidadABO(entidad);
            ComponenteDTO dto = ProductoMapper.boADTO(bo);

            if (ensamblajeActual != null && !validarCompatibilidad(dto, ensamblajeActual).isEmpty()) {
                continue;
            }

            dtos.add(dto);
        }

        return dtos;
    }

    private List<String> validarCompatibilidad(ComponenteDTO componenteNuevo, EnsamblajeDTO ensamblaje) {
        List<String> errores = new ArrayList<>();

        if (ensamblaje == null) {
            return errores;
        }

        ComponenteDTO placaMadre = ensamblaje.getComponente("Tarjeta Madre");
        ComponenteDTO procesador = ensamblaje.getComponente("Procesador");
        ComponenteDTO ram = ensamblaje.getComponente("RAM");
        ComponenteDTO gabinete = ensamblaje.getComponente("Gabinete");

        String categoriaNueva = componenteNuevo.getCategoria();

        switch (categoriaNueva) {
            case "Procesador":
                if (placaMadre != null && placaMadre.getSocket() != null &&
                    componenteNuevo.getSocket() != null &&
                    !placaMadre.getSocket().equals(componenteNuevo.getSocket())) {
                    errores.add("Socket incompatible con Tarjeta Madre (" + placaMadre.getSocket() + ")");
                }
                break;

            case "Tarjeta Madre":
                if (procesador != null && procesador.getSocket() != null &&
                    componenteNuevo.getSocket() != null &&
                    !procesador.getSocket().equals(componenteNuevo.getSocket())) {
                    errores.add("Socket incompatible con Procesador (" + procesador.getSocket() + ")");
                }
                if (ram != null && ram.getTipoRam() != null &&
                    componenteNuevo.getTipoRam() != null &&
                    !ram.getTipoRam().equals(componenteNuevo.getTipoRam())) {
                    errores.add("Tipo de RAM incompatible con RAM (" + ram.getTipoRam() + ")");
                }
                if (gabinete != null && gabinete.getFormFactor() != null &&
                    componenteNuevo.getFormFactor() != null &&
                    gabinete.getFormFactor().equals("Micro-ATX") &&
                    componenteNuevo.getFormFactor().equals("ATX")) {
                    errores.add("Placa ATX no cabe en gabinete Micro-ATX");
                }
                break;

            case "RAM":
                if (placaMadre != null && placaMadre.getTipoRam() != null &&
                    componenteNuevo.getTipoRam() != null &&
                    !placaMadre.getTipoRam().equals(componenteNuevo.getTipoRam())) {
                    errores.add("Tipo de RAM incompatible con Tarjeta Madre (" + placaMadre.getTipoRam() + ")");
                }
                break;

            case "Gabinete":
                if (placaMadre != null && placaMadre.getFormFactor() != null &&
                    componenteNuevo.getFormFactor() != null &&
                    componenteNuevo.getFormFactor().equals("Micro-ATX") &&
                    placaMadre.getFormFactor().equals("ATX")) {
                    errores.add("Gabinete Micro-ATX es muy pequeño para la Placa Madre (ATX)");
                }
                break;
        }

        return errores;
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


    @Override
    public ComponenteDTO convertirProductoADTO(String productoId) {
        if (productoId == null || productoId.trim().isEmpty()) {
            return null;
        }

        try {
            ProductoEntidad entidad = productoDAO.obtenerPorId(productoId);
            if (entidad == null) {
                return null;
            }

            ProductoBO bo = ProductoMapper.entidadABO(entidad);
            return ProductoMapper.boADTO(bo);
        } catch (Exception e) {
            System.err.println("Error al convertir producto a DTO: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<ComponenteDTO> obtenerProductosAleatorios(int cantidad) {
        try {
            List<ProductoEntidad> todasLasEntidades = productoDAO.obtenerTodos();

            if (todasLasEntidades == null || todasLasEntidades.isEmpty()) {
                return new ArrayList<>();
            }

            List<ProductoEntidad> entidadesAleatorias = seleccionarAleatorios(todasLasEntidades, cantidad);

            List<ComponenteDTO> dtos = new ArrayList<>();
            for (ProductoEntidad entidad : entidadesAleatorias) {
                ProductoBO bo = ProductoMapper.entidadABO(entidad);
                ComponenteDTO dto = ProductoMapper.boADTO(bo);
                dtos.add(dto);
            }

            return dtos;
        } catch (Exception e) {
            System.err.println("Error al obtener productos aleatorios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Selecciona elementos aleatorios de una lista, solo se usa para aggrear componentes el menu de inicio... de momento :b.
     *
     * @param lista Lista de elementos
     * @param cantidad Cantidad de elementos a seleccionar
     * @return Lista con elementos aleatorios
     */
    private <T> List<T> seleccionarAleatorios(List<T> lista, int cantidad) {
        if (lista.size() <= cantidad) {
            return new ArrayList<>(lista);
        }

        List<T> copia = new ArrayList<>(lista);
        List<T> seleccionados = new ArrayList<>();
        java.util.Random random = new java.util.Random();

        for (int i = 0; i < cantidad && !copia.isEmpty(); i++) {
            int indice = random.nextInt(copia.size());
            seleccionados.add(copia.remove(indice));
        }

        return seleccionados;
    }
}
