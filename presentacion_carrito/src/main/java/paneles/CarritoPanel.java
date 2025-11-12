package paneles;

import estilos.FontUtil;
import javax.swing.*;
import java.awt.*;

public class CarritoPanel extends PanelBase {
    private static String titulo = "Carrito";
    private JLabel tituloLbl;
    private OpcionEntregaPanel opcionEntregaPanel;
    private TotalPanel totalPanel;
    private metodoPagoPanel metodoPagoPanel;

    public CarritoPanel() {
        super();
        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);

        opcionEntregaPanel = new OpcionEntregaPanel();
        totalPanel = new TotalPanel();
        metodoPagoPanel = new metodoPagoPanel();

        // Panel Norte
        panelNorte.add(tituloLbl);

        // Panel Oeste
        panelOeste.add(opcionEntregaPanel);

        // Panel Este
        panelEste.add(totalPanel);

        panelEste.add(metodoPagoPanel);




    }

}
