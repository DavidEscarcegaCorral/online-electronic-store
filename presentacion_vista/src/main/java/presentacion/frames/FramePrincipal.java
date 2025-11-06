package presentacion.frames;

import estilos.Estilos;
import presentacion.panels.BarraNavegacion;

import javax.swing.*;
import java.awt.*;

public class FramePrincipal extends JFrame {
    private BarraNavegacion barraNavegacion;
    private JPanel panelContenido;

    public FramePrincipal() {
        setTitle("Electronic store");
        setSize(1200, 700);
        getContentPane().setBackground(Estilos.COLOR_BACKGROUND);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        barraNavegacion = new BarraNavegacion();

        // Añadir componentes
        add(barraNavegacion, BorderLayout.NORTH);
    }

    public BarraNavegacion getBarraNavegacion() {
        return barraNavegacion;
    }

    public void setPanelContenido(JPanel panelContenido) {
        if (this.panelContenido != null) {
            getContentPane().remove(this.panelContenido);
        }

        this.panelContenido = panelContenido;
        getContentPane().add(this.panelContenido, BorderLayout.CENTER);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public JPanel getPanelContenido() {
        return panelContenido;
    }


}
