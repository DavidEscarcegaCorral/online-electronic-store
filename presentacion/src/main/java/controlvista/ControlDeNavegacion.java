package controlvista;

import compartido.FramePrincipal;
import armadoPC.ArmarPcPanel;
import carrito.CarritoPanel;
import compartido.BarraNavegacion;
import menuprincipal.MenuPrincipalPanel;

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

        // Botones de "Barra de navegacion"
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


        // Botones de navegacion de "Armar PC"
        armarEquipoPantalla.getContinuarBtn().addActionListener(e -> navegarDireccion(1));
        armarEquipoPantalla.getRetrocederBtn().addActionListener(e -> navegarDireccion(-1));

        // Botones directos de "Armar PC"
        armarEquipoPantalla.getMenuComponentesPanel().getProcesadorBtn().addActionListener(e -> {
            navegarComponente(2);
        });
        armarEquipoPantalla.getMenuComponentesPanel().getTarjetaMadreBtn().addActionListener(e -> {
            navegarComponente(3);
        });
        armarEquipoPantalla.getMenuComponentesPanel().getMemoriaRAMBtn().addActionListener(e -> {
            navegarComponente(4);
        });
        armarEquipoPantalla.getMenuComponentesPanel().getAlmacenamientoBtn().addActionListener(e -> {
            navegarComponente(5);
        });
        armarEquipoPantalla.getMenuComponentesPanel().getUnidadSSDBtn().addActionListener(e -> {
            navegarComponente(6);
        });
        armarEquipoPantalla.getMenuComponentesPanel().getTarjetaDeVideoBtn().addActionListener(e -> {
            navegarComponente(7);
        });
        armarEquipoPantalla.getMenuComponentesPanel().getFuenteDePoderBtn().addActionListener(e -> {
            navegarComponente(8);
        });
        armarEquipoPantalla.getMenuComponentesPanel().getDisipadorBtn().addActionListener(e -> {
            navegarComponente(9);
        });
        armarEquipoPantalla.getMenuComponentesPanel().getVentiladorBtn().addActionListener(e -> {
            navegarComponente(10);
        });
        armarEquipoPantalla.getMenuComponentesPanel().getMonitorBtn().addActionListener(e -> {
            navegarComponente(11);
        });
        armarEquipoPantalla.getMenuComponentesPanel().getKitTecladoRatonBtn().addActionListener(e -> {
            navegarComponente(12);
        });
        armarEquipoPantalla.getMenuComponentesPanel().getRedBtn().addActionListener(e -> {
            navegarComponente(13);
        });
    }

    public void mostrarNuevaPantalla(JPanel nuevoPanel) {
        framePrincipal.setPanelContenido(nuevoPanel);
    }

    private void navegarDireccion(int direction) {
        int currentIndex = armarEquipoPantalla.getIndiceActual();
        int nuevoIndice = currentIndex + direction;

        if (nuevoIndice >= 2) {
            armarEquipoPantalla.añadirMenusNavegacion();
        }

        if (nuevoIndice < 2) {
            armarEquipoPantalla.eliminarMenusNavegacion();
        }

        if (nuevoIndice >= 0 && nuevoIndice < armarEquipoPantalla.getListaPasosArmado().length) {
            currentIndex = nuevoIndice;
            armarEquipoPantalla.getCardLayout().show(armarEquipoPantalla.getCardsPanel(), armarEquipoPantalla.getListaPasosArmado()[currentIndex]);
            armarEquipoPantalla.setIndiceActual(nuevoIndice);
            armarEquipoPantalla.getSubTItuloLabel().setTitulo(armarEquipoPantalla.getListaPasosArmado()[currentIndex]);
            armarEquipoPantalla.updateButtonState();
        }
    }

    public void navegarComponente(int componente) {
        if (componente >= 2) {
            armarEquipoPantalla.añadirMenusNavegacion();
        }

        if (componente < 2) {
            armarEquipoPantalla.eliminarMenusNavegacion();
        }

        armarEquipoPantalla.getCardLayout().show(armarEquipoPantalla.getCardsPanel(), armarEquipoPantalla.getListaPasosArmado()[componente]);
        armarEquipoPantalla.setIndiceActual(componente);
        armarEquipoPantalla.getSubTItuloLabel().setTitulo(armarEquipoPantalla.getListaPasosArmado()[componente]);
        armarEquipoPantalla.updateButtonState();
    }
}
