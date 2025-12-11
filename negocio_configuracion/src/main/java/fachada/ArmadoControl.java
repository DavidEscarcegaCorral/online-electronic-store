package fachada;

import dao.ConfiguracionDAO;
import dto.EnsamblajeDTO;
import entidades.ConfiguracionEntidad;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmadoControl {
    private static ArmadoControl instancia;
    private final ConfiguracionDAO configuracionDAO;

    private ArmadoControl() {
        this.configuracionDAO = new ConfiguracionDAO();
    }

    public static synchronized ArmadoControl getInstance() {
        if (instancia == null) {
            instancia = new ArmadoControl();
        }
        return instancia;
    }

    public String guardarConfiguracion(EnsamblajeDTO ensamblaje) {
        System.out.println("\n========== GUARDANDO CONFIGURACION EN CONTROL ==========");
        if (ensamblaje == null || ensamblaje.obtenerTodosComponentes().isEmpty()) {
            System.out.println("ERROR: Ensamblaje nulo o sin componentes");
            return null;
        }

        try {
            System.out.println("Creando nueva configuración...");
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

            System.out.println("\nPersistiendo configuración en BD...");
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
}

