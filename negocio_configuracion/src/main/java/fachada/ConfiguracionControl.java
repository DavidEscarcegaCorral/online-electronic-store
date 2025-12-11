package fachada;

import dao.ConfiguracionDAO;
import dto.EnsamblajeDTO;
import entidades.ConfiguracionEntidad;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Control de configuraciones.
 * Contiene la lógica de negocio para guardar/persistir configuraciones.
 * Transforma un armado temporal (EnsamblajeDTO) en una configuración persistente.
 */
public class ConfiguracionControl implements IConfiguracionControl {
    private static ConfiguracionControl instancia;
    private final ConfiguracionDAO configuracionDAO;

    private ConfiguracionControl() {
        this.configuracionDAO = new ConfiguracionDAO();
    }

    public static synchronized ConfiguracionControl getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionControl();
        }
        return instancia;
    }

    @Override
    public String guardarConfiguracion(EnsamblajeDTO ensamblaje) {
        System.out.println("\n========== GUARDANDO CONFIGURACION EN CONTROL ==========");
        if (ensamblaje == null || ensamblaje.obtenerTodosComponentes().isEmpty()) {
            System.out.println("ERROR: Ensamblaje nulo o sin componentes");
            return null;
        }

        try {
            System.out.println("Transformando armado temporal a configuración persistente...");
            ConfiguracionEntidad configuracion = transformarEnsamblajeAConfiguracion(ensamblaje);

            System.out.println("Persistiendo configuración en BD...");
            configuracionDAO.guardar(configuracion);
            System.out.println("Configuración guardada con ID: " + configuracion.getId());
            System.out.println("========== CONFIGURACION GUARDADA EXITOSAMENTE EN CONTROL ==========\n");

            return configuracion.getId().toString();
        } catch (Exception e) {
            System.err.println("ERROR al guardar configuración en control:");
            e.printStackTrace();
            return null;
        }
    }

    private ConfiguracionEntidad transformarEnsamblajeAConfiguracion(EnsamblajeDTO ensamblaje) {
        ConfiguracionEntidad configuracion = new ConfiguracionEntidad();
        configuracion.setNombre("Configuración " + java.time.LocalDateTime.now());

        List<Map<String, Object>> componentesList = new ArrayList<>();
        for (dto.ComponenteDTO comp : ensamblaje.obtenerTodosComponentes()) {
            Map<String, Object> compMap = new HashMap<>();
            compMap.put("categoria", comp.getCategoria());
            compMap.put("id", comp.getId());
            compMap.put("nombre", comp.getNombre());
            compMap.put("precio", comp.getPrecio());
            compMap.put("marca", comp.getMarca());
            componentesList.add(compMap);
        }

        configuracion.setComponentes(componentesList);
        configuracion.setPrecioTotal(ensamblaje.getPrecioTotal());

        System.out.println("Componentes en configuración: " + componentesList.size());
        System.out.println("Precio total: $" + ensamblaje.getPrecioTotal());

        return configuracion;
    }
}

