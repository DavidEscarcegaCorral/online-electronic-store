package fachada;

import dto.ComponenteDTO;
import dto.EnsamblajeDTO;
import objetosnegocio.ComponenteON;
import objetosnegocio.interfaces.IComponenteON;

import java.util.ArrayList;
import java.util.List;

public class ArmadoFacade implements IArmadoFacade {
    // Implementacion de un patron singleton
    private static ArmadoFacade instancia;

    public static synchronized ArmadoFacade getInstance() {
        if (instancia == null) {
            instancia = new ArmadoFacade();
        }
        return instancia;
    }

    private IComponenteON componenteON;
    private EnsamblajeDTO ensamblajeActual;

    /**
     * Constructor PRIVADO.
     * Obtiene las instancias de las capas de negocio (ON).
     */
    private ArmadoFacade() {

        this.componenteON = ComponenteON.getInstance();
    }

    @Override
    public EnsamblajeDTO iniciarNuevoEnsamblaje() {
        this.ensamblajeActual = new EnsamblajeDTO();
        return this.ensamblajeActual;
    }

    @Override
    public EnsamblajeDTO getEnsamblajeActual() {
        return this.ensamblajeActual;
    }

    @Override
    public List<ComponenteDTO> obtenerComponentesPorCategoria(String categoria) {
        return this.componenteON.obtenerPorCategoria(categoria);
    }

    @Override
    public List<String> agregarComponente(ComponenteDTO componente) {
        if (this.ensamblajeActual == null) {
            iniciarNuevoEnsamblaje();
        }

        List<String> errores = validarCompatibilidad(componente, this.ensamblajeActual);

        if (errores.isEmpty()) {
            this.ensamblajeActual.setComponente(componente.getCategoria(), componente);
        }

        return errores;
    }

    // Logica de negocio
    /**
     * Valida si un componente nuevo es compatible con el ensamblaje actual.
     * 
     * @param componenteNuevo El componente a probar.
     * @param ensamblaje      El ensamblaje actual.
     * @return Lista de errores. Vacía si es compatible.
     */
    private List<String> validarCompatibilidad(ComponenteDTO componenteNuevo, EnsamblajeDTO ensamblaje) {
        List<String> errores = new ArrayList<>();

        if (ensamblaje == null)
            return errores; // Si no hay ensamblaje, todo es compatible (primer componente o check de stock)

        // Obtenemos los componentes clave ya seleccionados
        ComponenteDTO placaMadre = ensamblaje.getComponente("Tarjeta Madre");
        ComponenteDTO procesador = ensamblaje.getComponente("Procesador");
        ComponenteDTO ram = ensamblaje.getComponente("RAM");
        ComponenteDTO gabinete = ensamblaje.getComponente("Gabinete");

        String categoriaNueva = componenteNuevo.getCategoria();

        switch (categoriaNueva) {
            case "Procesador":
                if (placaMadre != null && !placaMadre.getSocket().equals(componenteNuevo.getSocket())) {
                    errores.add("Socket incompatible con Tarjeta Madre (" + placaMadre.getSocket() + ")");
                }
                break;

            case "Tarjeta Madre":
                if (procesador != null && !procesador.getSocket().equals(componenteNuevo.getSocket())) {
                    errores.add("Socket incompatible con Procesador (" + procesador.getSocket() + ")");
                }
                // Validar Tipo de RAM contra RAM (si existe)
                if (ram != null && !ram.getTipoRam().equals(componenteNuevo.getTipoRam())) {
                    errores.add("Tipo de RAM incompatible con RAM (" + ram.getTipoRam() + ")");
                }
                if (gabinete != null && gabinete.getFormFactor().equals("Micro-ATX")
                        && componenteNuevo.getFormFactor().equals("ATX")) {
                    errores.add("Placa ATX no cabe en gabinete Micro-ATX");
                }
                break;

            case "RAM":
                if (placaMadre != null && !placaMadre.getTipoRam().equals(componenteNuevo.getTipoRam())) {
                    errores.add("Tipo de RAM incompatible con Tarjeta Madre (" + placaMadre.getTipoRam() + ")");
                }
                break;

            case "Gabinete":
                if (placaMadre != null && componenteNuevo.getFormFactor().equals("Micro-ATX")
                        && placaMadre.getFormFactor().equals("ATX")) {
                    errores.add("Gabinete Micro-ATX es muy pequeño para la Placa Madre (ATX)");
                }
                break;

            // Añadir mas validaciones despues
        }

        return errores;
    }

    @Override
    public boolean verificarStockSuficiente(String tipoUso) {
        // Lógica simplificada: verificar si hay al menos un componente de cada
        // categoría crítica
        String[] categoriasCriticas = { "Procesador", "Tarjeta Madre", "Memoria RAM", "Tarjeta de video",
                "Fuente de poder", "Gabinete" };
        for (String cat : categoriasCriticas) {
            List<ComponenteDTO> disponibles = obtenerComponentesCompatibles(cat, tipoUso);
            if (disponibles.isEmpty()) {
                // Si es oficina, tal vez no necesite GPU dedicada si el procesador tiene
                // gráficos integrados
                // Pero por simplicidad, asumiremos que todos necesitan todo por ahora, o
                // ajustamos:
                if (tipoUso.equalsIgnoreCase("OFFICE") && cat.equals("Tarjeta de video")) {
                    continue; // Oficina puede no requerir GPU dedicada (asumiendo iGPU)
                }
                System.out.println("Falta stock para: " + cat);
                return false;
            }
        }
        return true;
    }

    @Override
    public List<ComponenteDTO> obtenerComponentesCompatibles(String categoria, String tipoUso) {
        // 1. Obtener todos los de la categoría
        List<ComponenteDTO> todos = this.componenteON.obtenerPorCategoria(categoria);
        List<ComponenteDTO> compatibles = new ArrayList<>();

        // 2. Filtrar por compatibilidad con ensamblaje actual
        for (ComponenteDTO c : todos) {
            if (validarCompatibilidad(c, this.ensamblajeActual).isEmpty()) {
                compatibles.add(c);
            }
        }

        // 3. Filtrar por Tipo de Uso (Lógica de negocio simulada)
        // Por ejemplo, si es GAMER, solo mostrar componentes de gama media/alta o
        // ciertas series
        if (tipoUso != null) {
            compatibles = filtrarPorUso(compatibles, tipoUso);
        }

        return compatibles;
    }

    private List<ComponenteDTO> filtrarPorUso(List<ComponenteDTO> componentes, String tipoUso) {
        // Implementación simple de filtrado por uso
        // En un sistema real, esto podría basarse en tags o propiedades del componente
        List<ComponenteDTO> filtrados = new ArrayList<>();
        for (ComponenteDTO c : componentes) {
            boolean apto = true;
            if (tipoUso.equalsIgnoreCase("OFFICE")) {
                // Para oficina, evitar componentes muy caros o "Gamer" excesivos
                if (c.getNombre().toUpperCase().contains("RTX 3090") || c.getNombre().toUpperCase().contains("I9")) {
                    apto = false;
                }
            } else if (tipoUso.equalsIgnoreCase("GAMER")) {
                // Para gamer, evitar componentes muy básicos
                if (c.getNombre().toUpperCase().contains("CELERON") || c.getNombre().toUpperCase().contains("GT 710")) {
                    apto = false;
                }
            }
            if (apto) {
                filtrados.add(c);
            }
        }
        return filtrados;
    }

}
