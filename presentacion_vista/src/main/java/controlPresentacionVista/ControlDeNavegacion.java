package controlPresentacionVista;

import presentacion.frames.FramePrincipal;
import presentacion.panels.ArmarEquipoPanel;
import presentacion.panels.BarraNavegacion;
import presentacion.panels.MenuPrincipalPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlDeNavegacion {
    public MenuPrincipalPanel  menuPrincipalPanel;
    public ArmarEquipoPanel armarEquipo;
    public FramePrincipal framePrincipal;

    public ControlDeNavegacion(FramePrincipal framePrincipal){
       this.framePrincipal = framePrincipal;
       menuPrincipalPanel = new MenuPrincipalPanel();

       this.framePrincipal.setPanelContenido(menuPrincipalPanel);

       BarraNavegacion barra = this.framePrincipal.getBarraNavegacion();
       framePrincipal.setVisible(true);


        barra.getArmarPcLbl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarNuevoPanel(new ArmarEquipoPanel());
            }
        });
    }

    public void mostrarNuevoPanel(JPanel nuevoPanel){
        framePrincipal.getPanelContenido().removeAll();
        framePrincipal.setPanelContenido(nuevoPanel);
        framePrincipal.revalidate();
        framePrincipal.repaint();

    }
}
