package presentacion.frames;

import estilos.Estilos;
import presentacion.panels.BarraNavegacion;

import javax.swing.*;
import java.awt.*;

public class FramePrincipal extends JFrame {
    private JPanel panelContenedor;
    private BarraNavegacion barraNavegacion;
    private JPanel panelContenido;
    private JScrollPane scroll;

    public FramePrincipal() {
        setTitle("Electronic store");
        setSize(1360, 860);
        getContentPane().setBackground(Estilos.COLOR_BACKGROUND);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panelContenedor = new JPanel();
        panelContenedor.setLayout(new BorderLayout());
        panelContenedor.setOpaque(false);
        barraNavegacion = new BarraNavegacion();

        // Añadir componentes
        panelContenedor.add(barraNavegacion, BorderLayout.NORTH);
        scroll = new JScrollPane(panelContenedor);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.getHorizontalScrollBar().setUnitIncrement(20);
        scroll.setBorder(null);

        add(scroll);
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

}
