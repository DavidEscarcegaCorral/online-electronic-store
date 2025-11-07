package presentacion.panels;

import estilos.FontUtil;
import estilos.TituloLabel;

import javax.swing.*;
import java.awt.*;

public class CarritoPanel extends PanelBase {
    private static String titulo = "Carrito";
    private JLabel tituloLbl;

    public CarritoPanel() {
        super();
        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);

        // Panel Norte
        panelNorte.add(tituloLbl);
    }

}
