package presentacion.panels.iniciarSesion;

import estilos.textfields.PsswrdFieldPh;
import estilos.textfields.TxtFieldPh;

import javax.swing.*;
import java.awt.*;

public class iniciarSesionPanel extends JPanel {
    private JPanel panelCentro;
    private TxtFieldPh correoElectronicoTxt;
    private TxtFieldPh nombreTxt;
    private PsswrdFieldPh contraseñaTxt;
    private PsswrdFieldPh confirmarContraseñaTxt;

    private JButton btnEntrar;
    private JButton btnRegistro;

    public iniciarSesionPanel() {
        super();
        setLayout(new BorderLayout(5, 5));
        iniciarComponentes();
        panelCentro = new  JPanel(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.add(correoElectronicoTxt);
        panelCentro.add(contraseñaTxt);
        add(panelCentro, BorderLayout.CENTER);

    }

    public void iniciarComponentes() {
        correoElectronicoTxt = new TxtFieldPh("Correo electronico", 100, 40, 12, 10);
        nombreTxt = new TxtFieldPh("Nombre Completo", 100, 40, 12, 10);
        contraseñaTxt = new PsswrdFieldPh("Controaseña", 100, 40, 12, 10);
        confirmarContraseñaTxt = new PsswrdFieldPh("Confirmar Controaseña", 100, 12, 16, 10);


    }


}
