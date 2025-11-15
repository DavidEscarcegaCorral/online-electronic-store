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
     * @param componenteNuevo El componente a probar.
     * @param ensamblaje El ensamblaje actual.
     * @return Lista de errores. Vacía si es compatible.
     */
    private List<String> validarCompatibilidad(ComponenteDTO componenteNuevo, EnsamblajeDTO ensamblaje) {
        List<String> errores = new ArrayList<>();

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
                if (gabinete != null && gabinete.getFormFactor().equals("Micro-ATX") && componenteNuevo.getFormFactor().equals("ATX")) {
                    errores.add("Placa ATX no cabe en gabinete Micro-ATX");
                }
                break;

            case "RAM":
                if (placaMadre != null && !placaMadre.getTipoRam().equals(componenteNuevo.getTipoRam())) {
                    errores.add("Tipo de RAM incompatible con Tarjeta Madre (" + placaMadre.getTipoRam() + ")");
                }
                break;

            case "Gabinete":
                if (placaMadre != null && componenteNuevo.getFormFactor().equals("Micro-ATX") && placaMadre.getFormFactor().equals("ATX")) {
                    errores.add("Gabinete Micro-ATX es muy pequeño para la Placa Madre (ATX)");
                }
                break;

            // Añadir mas validaciones despues
        }

        return errores;
    }
}
