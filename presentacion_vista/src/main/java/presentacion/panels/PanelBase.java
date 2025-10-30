package presentacion.panels;

import controlPresentacionVista.ControlNavegacion;
import javax.swing.*;
import java.awt.*;

public class PanelBase extends JPanel {
    protected ControlNavegacion controlador;
    protected JPanel panelNorte;
    protected JPanel panelCentro;
    protected BarraNavegacion barraNavegacion;
    protected PanelDestacados panelDestacados;

    public PanelBase(ControlNavegacion controlador) {
        this.controlador = controlador;
        
        setOpaque(false);

        panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        barraNavegacion = new BarraNavegacion(controlador);
        panelDestacados = new PanelDestacados();

        //Panel norte
        panelNorte.setPreferredSize(new Dimension(950, 200));
        panelNorte.add(barraNavegacion);
        add(panelNorte);

        //Panel centro
        panelCentro.setPreferredSize(new Dimension(1100, 450));
        panelCentro.add(panelDestacados);
        add(panelCentro);
    }
}