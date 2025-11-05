package presentacion.panels;

import estilos.TituloLabel;

import javax.swing.*;

public class CarritoPanel extends PanelBase {
    private static String titulo = "Carrito";

    public CarritoPanel(){
        super();
        tituloLabel = new TituloLabel(titulo);
        panelNorte.add(tituloLabel);
    }


}
