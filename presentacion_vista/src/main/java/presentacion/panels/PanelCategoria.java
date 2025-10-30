package presentacion.panels;

import controlPresentacionVista.ControlNavegacion;
import javax.swing.*;
import java.awt.*;

public class PanelCategoria extends JPanel{
    private ControlNavegacion controlador;
    
    public PanelCategoria(ControlNavegacion controlador){
        this.controlador = controlador;

        JLabel label = new JLabel("Panel de Categorias (en desarrollo)");
        add(label);
    }
}
