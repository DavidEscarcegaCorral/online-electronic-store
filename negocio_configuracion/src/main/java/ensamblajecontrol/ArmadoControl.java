package ensamblajecontrol;

import dao.ConfiguracionDAO;
import dto.ComponenteDTO;
import dto.EnsamblajeDTO;
import entidades.ConfiguracionEntidad;
import objetosNegocio.componenteON.ComponenteON;
import objetosNegocio.componenteON.IComponenteON;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmadoControl implements IArmadoControl {
    private final ConfiguracionDAO configuracionDAO;
    private final IComponenteON componenteON;
    private EnsamblajeDTO ensamblajeActual;

    public ArmadoControl() {
        this.configuracionDAO = new ConfiguracionDAO();
        this.componenteON = ComponenteON.getInstance();
        this.ensamblajeActual = new EnsamblajeDTO();
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
            ConfiguracionEntidad configuracion = new ConfiguracionEntidad();
            configuracion.setNombre("Configuración " + LocalDateTime.now());
            configuracion.setUsuarioId(usuarioId);

            List<Map<String, Object>> componentesList = new ArrayList<>();
            for (ComponenteDTO comp : ensamblaje.obtenerTodosComponentes()) {
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

            configuracionDAO.guardar(configuracion);

            limpiarEnsamblaje();

            return configuracion.getId().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    private List<String> validarCompatibilidad(ComponenteDTO componenteNuevo, EnsamblajeDTO ensamblaje) {
        List<String> errores = new ArrayList<>();

        if (ensamblaje == null)
            return errores;

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
        }

        return errores;
    }

    @Override
    public boolean verificarStockSuficiente(String tipoUso) {
        List<String> categoriasCriticas = obtenerCategoriasCriticas(tipoUso);

        for (String cat : categoriasCriticas) {
            List<ComponenteDTO> disponibles = obtenerComponentesCompatibles(cat, tipoUso);
            if (disponibles.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obtiene las categorías críticas según el tipo de uso.
     *
     * Categorías críticas (obligatorias):
     * - Gaming/Diseño/Custom: Procesador, Tarjeta Madre, RAM, Tarjeta de video, Almacenamiento, Fuente de poder, Gabinete, Disipador
     * - Office: Procesador, Tarjeta Madre, RAM, Almacenamiento, Fuente de poder, Gabinete, Disipador (SIN Tarjeta de video)
     *
     * Categorías opcionales (para todos): Ventilador, Monitor, Kit de teclado/ratón, Redes e internet
     */
    private List<String> obtenerCategoriasCriticas(String tipoUso) {
        List<String> categoriasCriticas = new ArrayList<>();

        categoriasCriticas.add("Procesador");
        categoriasCriticas.add("Tarjeta Madre");
        categoriasCriticas.add("RAM");

        if (!tipoUso.equalsIgnoreCase("OFFICE")) {
            categoriasCriticas.add("Tarjeta de video");
        }

        categoriasCriticas.add("Almacenamiento");
        categoriasCriticas.add("Fuente de poder");
        categoriasCriticas.add("Gabinete");
        categoriasCriticas.add("Disipador");

        return categoriasCriticas;
    }

    @Override
    public List<ComponenteDTO> obtenerComponentesCompatibles(String categoria, String tipoUso) {
        List<ComponenteDTO> todos = this.componenteON.obtenerPorCategoria(categoria);
        List<ComponenteDTO> compatibles = new ArrayList<>();

        for (ComponenteDTO c : todos) {
            if (validarCompatibilidad(c, this.ensamblajeActual).isEmpty()) {
                compatibles.add(c);
            }
        }

        if (tipoUso != null && !tipoUso.isEmpty()) {
            compatibles = filtrarPorUsoDesdeComponente(compatibles, tipoUso);
        }

        return compatibles;
    }

    private List<ComponenteDTO> filtrarPorUsoDesdeComponente(List<ComponenteDTO> componentes, String tipoUso) {
        List<ComponenteDTO> filtrados = new ArrayList<>();
        for (ComponenteDTO c : componentes) {
            if (esAptoPara(c, tipoUso)) {
                filtrados.add(c);
            }
        }
        return filtrados;
    }

    private boolean esAptoPara(ComponenteDTO componente, String tipoUso) {
        String usoRecomendado = componente.getUsoRecomendado();
        String gama = componente.getGama();

        if (usoRecomendado == null || gama == null) {
            return true;
        }

        if (tipoUso.equalsIgnoreCase("OFFICE")) {
            return usoRecomendado.equalsIgnoreCase("OFFICE") ||
                   usoRecomendado.equalsIgnoreCase("MULTIFUNCION") ||
                   gama.equalsIgnoreCase("BAJA") ||
                   gama.equalsIgnoreCase("MEDIA");
        } else if (tipoUso.equalsIgnoreCase("GAMER")) {
            return usoRecomendado.equalsIgnoreCase("GAMING") ||
                   usoRecomendado.equalsIgnoreCase("MULTIFUNCION") ||
                   gama.equalsIgnoreCase("ALTA");
        }

        return true;
    }

    @Override
    public boolean puedeVolverAtras(String categoria) {
        if (this.ensamblajeActual == null) {
            return false;
        }
        return this.ensamblajeActual.tieneComponente(categoria);
    }

    @Override
    public void removerComponente(String categoria) {
        if (this.ensamblajeActual != null) {
            this.ensamblajeActual.removerComponente(categoria);
        }
    }

    @Override
    public List<String> revalidarEnsamblaje() {
        List<String> errores = new ArrayList<>();
        if (this.ensamblajeActual == null) {
            return errores;
        }

        List<ComponenteDTO> componentes = this.ensamblajeActual.obtenerTodosComponentes();
        for (ComponenteDTO comp : componentes) {
            List<String> erroresComp = validarCompatibilidad(comp, this.ensamblajeActual);
            if (!erroresComp.isEmpty()) {
                errores.add(comp.getCategoria() + ": " + String.join(", ", erroresComp));
            }
        }

        return errores;
    }

    @Override
    public ComponenteDTO getComponenteSeleccionado(String categoria) {
        if (this.ensamblajeActual == null) return null;
        return this.ensamblajeActual.getComponente(categoria);
    }

    @Override
    public void limpiarEnsamblaje() {
        this.ensamblajeActual = new EnsamblajeDTO();
    }

    @Override
    public void removerComponentesPosteriores(String categoria) {
        if (this.ensamblajeActual == null) return;

        String[] ordenConstruccion = {
            "Procesador", "Tarjeta Madre", "RAM", "Tarjeta de video",
            "Almacenamiento", "Fuente de poder", "Gabinete", "Disipador",
            "Ventilador", "Monitor", "Kit de teclado/raton", "Redes e internet"
        };

        int indiceCategoria = -1;
        for (int i = 0; i < ordenConstruccion.length; i++) {
            if (ordenConstruccion[i].equals(categoria)) {
                indiceCategoria = i;
                break;
            }
        }

        if (indiceCategoria >= 0) {
            for (int i = indiceCategoria + 1; i < ordenConstruccion.length; i++) {
                this.ensamblajeActual.removerComponente(ordenConstruccion[i]);
            }
        }
    }
}

