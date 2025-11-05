package presentacion.panels;

import estilos.Boton;
import estilos.Estilos;
import estilos.TituloLabel;

import javax.swing.*;
import java.awt.*;

public class PanelBase extends JPanel {
    protected JPanel panelNorte;
    protected JPanel panelCentro;
    protected JPanel panelSur;
    protected JPanel panelEste;
    protected JPanel panelOeste;
    protected JButton atrasBtn;
    protected TituloLabel tituloLabel;

    protected Boton boton;

    public PanelBase() {
        setOpaque(false);

        panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        panelCentro = new JPanel();
        panelCentro.setOpaque(false);

        add(panelNorte);
        add(panelCentro);

    }

}
