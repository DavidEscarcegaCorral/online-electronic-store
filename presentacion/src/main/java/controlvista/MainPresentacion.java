package controlvista;

import compartido.FramePrincipal;
import dao.DatabaseInitializer;

import javax.swing.*;

public class MainPresentacion {
    public static void main(String[] args) {
        System.out.println("     ONLINE ELECTRONIC STORE HIGHSPECS - INICIANDO    ");

        // Inicializar la base de datos PRIMERO, antes de cualquier cosa de Swing
        System.out.println("Inicializando base de datos...");
        boolean bdInicializada = DatabaseInitializer.ejecutarInicializacion();

        if (!bdInicializada) {
            System.err.println("ERROR: No se pudo inicializar la base de datos.");
            System.err.println("Por favor, verifica que MongoDB este ejecutandose.");
            System.exit(1);
            return;
        }

        System.out.println("Base de datos lista");
        System.out.println("\nIniciando aplicacion...\n");

        // Ahora sí, iniciar la interfaz gráfica
        try {
            FramePrincipal framePrincipal = new FramePrincipal();
            ControlDeNavegacion controlDeNavegacion = new ControlDeNavegacion(framePrincipal);

            framePrincipal.setExtendedState(JFrame.MAXIMIZED_BOTH);
            framePrincipal.setVisible(true);

            System.out.println("Aplicacion iniciada correctamente\n");
        } catch (Exception e) {
            System.err.println("ERROR al iniciar la aplicacion:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
