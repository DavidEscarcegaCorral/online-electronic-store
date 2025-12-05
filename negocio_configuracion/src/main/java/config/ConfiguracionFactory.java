package config;

import fachada.IVentaFacade;
import fachada.VentaFacade;
import fachada.IArmadoFacade;
import fachada.ArmadoFacade;

/**
 * Fábrica simple para obtener instancias singleton de las fachadas de negocio.
 */
public class ConfiguracionFactory {
    private static IVentaFacade ventaFacade;

    public static synchronized IVentaFacade getVentaFacade() {
        if (ventaFacade == null) {
            ventaFacade = new VentaFacade();
        }
        return ventaFacade;
    }

    public static IArmadoFacade getArmadoFacade() {
        return ArmadoFacade.getInstance();
    }
}

