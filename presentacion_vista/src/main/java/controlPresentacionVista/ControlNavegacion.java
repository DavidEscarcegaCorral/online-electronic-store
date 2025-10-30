package controlPresentacionVista;

import estilos.Estilos;
import java.awt.CardLayout;
import javax.swing.*;
import presentacion.frames.FramePrincipal;
import presentacion.panels.PanelBase;
import presentacion.panels.PanelCategoria;

public class ControlNavegacion {
    private FramePrincipal frame;
    private JPanel panelPrincipal;
    private PanelBase panelBase;
    private PanelCategoria panelCategoria;
    private CardLayout layout;
    
    public ControlNavegacion(){
        frame = new FramePrincipal();
        layout = new CardLayout();
        panelPrincipal = new JPanel(layout);
        
        panelBase = new PanelBase(this);
        panelCategoria = new PanelCategoria(this);
        
        panelPrincipal.add(panelBase, "Base");
        panelPrincipal.add(panelCategoria, "Categoria");
        panelPrincipal.setBackground(Estilos.COLOR_BACKGROUND);
        
        frame.setContentPane(panelPrincipal);
        
        mostrarPanel("Base");
        
        frame.setVisible(true);
    }
    
    public final void mostrarPanel(String nombre){
        layout.show(panelPrincipal, nombre);
    }
}
