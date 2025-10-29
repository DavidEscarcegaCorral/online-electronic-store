package presentacion.panels;

import javax.swing.*;
import java.awt.*;

public class PanelBase extends JPanel {
    protected JPanel panelNorte;
    protected JPanel panelCentro;
    protected BarraNavegacion barraNavegacion;

    public PanelBase() {
        setOpaque(false);

        panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        barraNavegacion = new BarraNavegacion();

        //Panel norte
        panelNorte.setPreferredSize(new Dimension(950, 200));
        panelNorte.add(barraNavegacion);
        add(panelNorte);

    }
}
