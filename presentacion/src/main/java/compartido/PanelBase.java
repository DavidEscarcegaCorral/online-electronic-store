package compartido;

import javax.swing.*;
import java.awt.*;

public class PanelBase extends JPanel {
    protected JPanel panelNorte;
    protected JPanel panelCentro;
    protected JPanel panelSur;
    protected JPanel panelEste;
    protected JPanel panelOeste;

    public PanelBase() {
        setOpaque(false);
        setLayout(new BorderLayout());

        panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        panelNorte.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelNorte.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        panelCentro = new JPanel();
        panelCentro.setOpaque(false);

        panelSur = new JPanel();
        panelSur.setOpaque(false);

        panelEste = new JPanel();
        panelEste.setOpaque(false);
        panelEste.setLayout(new FlowLayout(FlowLayout.LEFT));

        panelOeste = new JPanel();
        panelOeste.setOpaque(false);

        add(panelNorte,  BorderLayout.NORTH);
        add(panelCentro,   BorderLayout.CENTER);
        add(panelSur,    BorderLayout.SOUTH);
        add(panelEste,    BorderLayout.EAST);
        add(panelOeste,    BorderLayout.WEST);

    }

    public JPanel getPanelCentro() {
        return panelCentro;
    }

    public void setPanelCentro(JPanel panelCentro) {
        this.panelCentro = panelCentro;
    }


}
