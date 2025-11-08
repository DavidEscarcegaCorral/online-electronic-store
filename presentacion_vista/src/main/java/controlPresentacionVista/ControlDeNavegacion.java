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

    public ControlDeNavegacion(FramePrincipal framePrincipal) {
        this.framePrincipal = framePrincipal;
        menuPrincipalPanel = new MenuPrincipalPanel();
        armarEquipoPantalla = new ArmarPcPanel();
        carritoPantalla = new CarritoPanel();

        this.framePrincipal.setPanelContenido(menuPrincipalPanel);

        BarraNavegacion barra = this.framePrincipal.getBarraNavegacion();
        framePrincipal.setVisible(true);

        barra.getBoton().addActionListener(e -> {
            mostrarNuevaPantalla(menuPrincipalPanel);
        });

        barra.getArmarPcLbl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarNuevaPantalla(armarEquipoPantalla);
            }
        });

        barra.getCarritoLbl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarNuevaPantalla(carritoPantalla);
            }
        });

        armarEquipoPantalla.getContinuarBtn().addActionListener(e -> navegarComponentes(1));
        armarEquipoPantalla.getRetrocederBtn().addActionListener(e -> navegarComponentes(-1));

    }

    public void mostrarNuevaPantalla(JPanel nuevoPanel) {
        framePrincipal.setPanelContenido(nuevoPanel);
    }

    private void navegarComponentes(int direction) {
        int currentIndex = armarEquipoPantalla.getCurrentIndex();
        int newIndex = currentIndex + direction;
        if (newIndex >=2){
            armarEquipoPantalla.añadirMenusNavegacion();
        }
        if  (newIndex <2){
            armarEquipoPantalla.getPanelEste().removeAll();
            armarEquipoPantalla.getPanelEste().revalidate();
            armarEquipoPantalla.getPanelEste().repaint();


            armarEquipoPantalla.getPanelOeste().removeAll();
            armarEquipoPantalla.getPanelOeste().revalidate();
            armarEquipoPantalla.getPanelOeste().repaint();
        }

        if (newIndex >= 0 && newIndex < armarEquipoPantalla.getCardNames().size()) {
            currentIndex = newIndex;
            armarEquipoPantalla.getCardLayout().show(armarEquipoPantalla.getCardsPanel(), armarEquipoPantalla.getCardNames().get(currentIndex));
            armarEquipoPantalla.setCurrentIndex(newIndex);

            armarEquipoPantalla.updateButtonState();
        }
    }
}
