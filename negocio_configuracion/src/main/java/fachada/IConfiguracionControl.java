package fachada;

import dto.EnsamblajeDTO;

/**
 * Interfaz que define el contrato para el control de configuraciones.
 * Define todas las operaciones de persistencia relacionadas con configuraciones.
 */
public interface IConfiguracionControl {

    /**
     * Guarda un armado (ensamblaje) como una configuración persistente en la BD.
     * Transforma el EnsamblajeDTO temporal en una ConfiguracionEntidad y la persiste.
     *
     * @param ensamblaje El armado (EnsamblajeDTO) a guardar como configuración.
     * @return El ID de la configuración guardada, o null si hubo error.
     */
    String guardarConfiguracion(EnsamblajeDTO ensamblaje);
}

