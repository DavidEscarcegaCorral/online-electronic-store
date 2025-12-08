package fachada;

import dto.ComponenteDTO;
import dto.EnsamblajeDTO;
import objetosNegocio.componenteON.ComponenteON;
import objetosNegocio.componenteON.IComponenteON;

import java.util.ArrayList;
import java.util.List;

public class ArmadoFacade implements IArmadoFacade {
    private static ArmadoFacade instancia;

    public static synchronized ArmadoFacade getInstance() {
        if (instancia == null) {
            instancia = new ArmadoFacade();
        }
        return instancia;
    }

    private final IComponenteON componenteON;
    private EnsamblajeDTO ensamblajeActual;

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
        String[] categoriasCriticas = {"Procesador", "Tarjeta Madre", "Memoria RAM", "Tarjeta de video",
                "Fuente de poder", "Gabinete"};
        for (String cat : categoriasCriticas) {
            List<ComponenteDTO> disponibles = obtenerComponentesCompatibles(cat, tipoUso);
            if (disponibles.isEmpty()) {
                if (tipoUso.equalsIgnoreCase("OFFICE") && cat.equals("Tarjeta de video")) {
                    continue;
                }
                return false;
            }
        }
        return true;
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

        if (tipoUso != null) {
            compatibles = filtrarPorUso(compatibles, tipoUso);
        }

        return compatibles;
    }

    private List<ComponenteDTO> filtrarPorUso(List<ComponenteDTO> componentes, String tipoUso) {
        List<ComponenteDTO> filtrados = new ArrayList<>();
        for (ComponenteDTO c : componentes) {
            boolean apto = true;
            if (tipoUso.equalsIgnoreCase("OFFICE")) {
                if (c.getNombre().toUpperCase().contains("RTX 3090") || c.getNombre().toUpperCase().contains("I9")) {
                    apto = false;
                }
            } else if (tipoUso.equalsIgnoreCase("GAMER")) {
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
}

