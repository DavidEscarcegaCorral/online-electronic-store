package controlvista;

import javax.swing.JPanel;

/**
 * Interfaz para el controlador principal de navegación entre pantallas.
 * Define el contrato para gestionar el flujo de la aplicación y la comunicación
 * entre la vista y los subsistemas de negocio.
 */
public interface IControlDeNavegacion {

    /**
     * Obtiene el panel actual de la pantalla activa.
     * @return El JPanel que representa la pantalla actual.
     */
    JPanel getPanelActual();

    /**
     * Navega a un índice de paso específico en el flujo de armado.
     * @param indice El índice del paso al que navegar.
     */
    void navegarAIndice(int indice);

    /**
     * Avanza al siguiente paso en el flujo de armado.
     */
    void avanzarPaso();

    /**
     * Retrocede al paso anterior en el flujo de armado.
     */
    void retrocederPaso();
}
