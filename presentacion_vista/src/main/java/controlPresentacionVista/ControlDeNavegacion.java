package controlPresentacionVista;

import presentacion.frames.FramePrincipal;
import presentacion.panels.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlDeNavegacion {
    private FramePrincipal framePrincipal;
    private MenuPrincipalPanel  menuPrincipalPanel;
    private ArmarEquipoPanel armarEquipoPantalla;
    private CarritoPanel carritoPantalla;
    private PanelBase panelBase;
    private JPanel panelAnterior;

    public ControlDeNavegacion(FramePrincipal framePrincipal){
       this.framePrincipal = framePrincipal;
       panelBase = new PanelBase();
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
                panelBase = armarEquipoPantalla;
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
        framePrincipal.setPanelContenido(nuevoPanel);
    }
}
