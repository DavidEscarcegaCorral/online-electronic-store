package compartido;


import compartido.estilos.Estilos;
import compartido.estilos.scroll.ScrollPaneCustom;

import javax.swing.*;
import java.awt.*;

public class FramePrincipal extends JFrame {
    private JPanel panelContenedor;
    private BarraNavegacion barraNavegacion;
    private JPanel panelContenido;
    private ScrollPaneCustom scrollCustom;

    public FramePrincipal() {
        setTitle("Electronic store");
        getContentPane().setBackground(Estilos.COLOR_BACKGROUND);
        setSize(1380, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel contenedor
        panelContenedor = new JPanel();
        panelContenedor.setLayout(new BorderLayout());
        panelContenedor.setOpaque(false);
        barraNavegacion = new BarraNavegacion();

        // ScrollPane
        scrollCustom = new ScrollPaneCustom(panelContenedor);

        // Añadir componentes
        panelContenedor.add(barraNavegacion, BorderLayout.NORTH);

        add(scrollCustom, BorderLayout.CENTER);
    }

    public BarraNavegacion getBarraNavegacion() {
        return barraNavegacion;
    }

    public void setPanelContenido(JPanel panelContenido) {
        if (this.panelContenido != null) {
            panelContenedor.remove(this.panelContenido);
        }

        this.panelContenido = panelContenido;
        panelContenedor.add(this.panelContenido, BorderLayout.CENTER);
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    public JPanel getPanelContenido() {
        return panelContenido;
    }

}
