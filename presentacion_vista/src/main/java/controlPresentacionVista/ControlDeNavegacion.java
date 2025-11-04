package controlPresentacionVista;

import presentacion.frames.FramePrincipal;
import presentacion.panels.ArmarEquipoPanel;
import presentacion.panels.BarraNavegacion;
import presentacion.panels.CarritoPanel;
import presentacion.panels.MenuPrincipalPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlDeNavegacion {
    public FramePrincipal framePrincipal;
    public MenuPrincipalPanel  menuPrincipalPanel;
    public ArmarEquipoPanel armarEquipoPantalla;
    public CarritoPanel carritoPantalla;

    public ControlDeNavegacion(FramePrincipal framePrincipal){
       this.framePrincipal = framePrincipal;
       menuPrincipalPanel = new MenuPrincipalPanel();
       armarEquipoPantalla = new ArmarEquipoPanel();
       carritoPantalla =  new CarritoPanel();

       this.framePrincipal.setPanelContenido(menuPrincipalPanel);

       BarraNavegacion barra = this.framePrincipal.getBarraNavegacion();
       framePrincipal.setVisible(true);


        barra.getArmarPcLbl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarNuevoPanel(armarEquipoPantalla);
            }
        });

        barra.getCarritoLbl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarNuevoPanel(carritoPantalla);
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
