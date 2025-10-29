package presentacion.frames;

import estilos.Estilos;
import presentacion.panels.ArmarEquipoPanel;
import presentacion.panels.MenuPrincipalPanel;
import presentacion.panels.PanelBase;

import javax.swing.*;
import java.awt.*;

public class FramePrincipal extends JFrame {
    private PanelBase panelPrincipal;

    public FramePrincipal() {
        setTitle("Electronic store");
        setSize(1200, 800);
        getContentPane().setBackground(Estilos.COLOR_BACKGROUND);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        panelPrincipal = new PanelBase();

        //Añadir componentes
        add(panelPrincipal);

        setVisible(true);
    }
}
