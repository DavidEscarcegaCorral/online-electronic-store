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
    protected Boton atrasBtn;
    protected TituloLabel tituloLabel;

    protected Boton boton;

    public PanelBase() {
        setOpaque(false);
        setLayout(new BorderLayout());
//        atrasBtn = new Boton("←", 50, 50, 15, 25, Color.white, Estilos.COLOR_ATRAS_BOTON, Estilos.COLOR_ATRAS_BOTON_HOOVER);

        panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelSur = new JPanel();
        panelSur.setOpaque(false);
        panelEste = new JPanel();
        panelEste.setOpaque(false);
        panelOeste = new JPanel();
        panelOeste.setOpaque(false);

        add(panelNorte,  BorderLayout.NORTH);
        add(panelCentro,   BorderLayout.CENTER);
        add(panelSur,    BorderLayout.SOUTH);
        add(panelEste,    BorderLayout.EAST);
        add(panelOeste,    BorderLayout.WEST);

    }

    public Boton getAtrasBtn() {
        return atrasBtn;
    }

}
