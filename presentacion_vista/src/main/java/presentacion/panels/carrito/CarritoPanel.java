package presentacion.panels.carrito;

import estilos.FontUtil;
import presentacion.panels.PanelBase;

import javax.swing.*;
import java.awt.*;

public class CarritoPanel extends PanelBase {
    private static String titulo = "Carrito";
    private JLabel tituloLbl;
    private OpcionEntregaMenu opcionEntregaMenu;

    public CarritoPanel() {
        super();
        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);

        opcionEntregaMenu = new OpcionEntregaMenu();

        // Panel Norte
        panelNorte.add(tituloLbl);

        // Panel Oeste
        panelOeste.add(opcionEntregaMenu);




    }

}
