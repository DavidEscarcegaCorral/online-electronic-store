package iniciarsesion;

import compartido.PanelBase;
import compartido.estilos.Boton;
import compartido.estilos.Estilos;
import compartido.estilos.textfields.PsswrdFieldPh;
import compartido.estilos.textfields.TxtFieldPh;
import Sesion.SesionManager;
import javax.swing.*;
import java.awt.*;

public class InicioDeSesionPanel extends PanelBase {
    private JPanel iniciarPanel;
    private JPanel registroPanel;

    private TxtFieldPh correoTextField;
    private TxtFieldPh nombreCompletoTextField;

    private PsswrdFieldPh contraseniaTextField;
    private PsswrdFieldPh confirmarContraseniaTextField;

    private Boton iniciarSesionBtn;
    private Boton registrarseBtn;

    private SesionManager sesion;
    public InicioDeSesionPanel() {
        sesion = SesionManager.getInstance();
        setOpaque(false);
        iniciarPanel = new JPanel();
        registroPanel = new JPanel();

        correoTextField = new TxtFieldPh("Correo Electronico", 130, 50, 12, 10);
        nombreCompletoTextField = new TxtFieldPh("Nombre completo", 130, 50, 12, 10);

        contraseniaTextField = new PsswrdFieldPh("Contraseñaa", 130, 50, 12, 10);
        confirmarContraseniaTextField = new PsswrdFieldPh("Confirmar Contraseñaa", 130, 50, 12, 10);

        iniciarSesionBtn = new Boton("Iniciar sesion", 130, 30, 5,5, Color.WHITE, Estilos.COLOR_ENFASIS, Estilos.COLOR_ENFASIS_HOOVER);
        registrarseBtn = new Boton("Registrarse", 130, 30, 5,5, Color.WHITE, Estilos.COLOR_ENFASIS, Estilos.COLOR_ENFASIS_HOOVER);

    }
}
