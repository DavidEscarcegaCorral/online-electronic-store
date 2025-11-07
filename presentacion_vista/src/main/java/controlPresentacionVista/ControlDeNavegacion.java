package controlPresentacionVista;

import presentacion.frames.FramePrincipal;
import presentacion.panels.*;
import presentacion.panels.armarpc.ArmarPcPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlDeNavegacion {
    private FramePrincipal framePrincipal;
    private MenuPrincipalPanel menuPrincipalPanel;
    private ArmarPcPanel armarEquipoPantalla;
    private CarritoPanel carritoPantalla;
    private JPanel panelAnterior;

    public ControlDeNavegacion(FramePrincipal framePrincipal) {
        this.framePrincipal = framePrincipal;
        menuPrincipalPanel = new MenuPrincipalPanel();
        armarEquipoPantalla = new ArmarPcPanel();
        carritoPantalla = new CarritoPanel();

        this.framePrincipal.setPanelContenido(menuPrincipalPanel);

        BarraNavegacion barra = this.framePrincipal.getBarraNavegacion();
        framePrincipal.setVisible(true);

        barra.getBoton().addActionListener(e -> {
            mostrarNuevoPanel(menuPrincipalPanel);
        });

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

    public void mostrarNuevoPanel(JPanel nuevoPanel) {
        framePrincipal.setPanelContenido(nuevoPanel);
    }

    public void armarPcNavegacion(JPanel nuevoPanel) {
        armarEquipoPantalla.setPanelCentro(nuevoPanel);

    }
}
