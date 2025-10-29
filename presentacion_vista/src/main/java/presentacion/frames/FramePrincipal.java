package presentacion.frames;

import presentacion.panels.ArmarEquipoPanel;
import presentacion.panels.MenuPrincipalPanel;

import javax.swing.*;
import java.awt.*;

public class FramePrincipal extends JFrame {
    private MenuPrincipalPanel menuPrincipalPanel;
    private ArmarEquipoPanel  armarEquipoPanel;

    public FramePrincipal() {
        setTitle("Electronic store");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        menuPrincipalPanel = new MenuPrincipalPanel();
        armarEquipoPanel = new ArmarEquipoPanel();


        add(armarEquipoPanel);

        setVisible(true);
    }
}
