package main;

import controlnavegacion.ControlDeNavegacion;
import frames.FramePrincipal;

import javax.swing.*;

public class MainPresentacion {
    public static void main(String[] args) {
        FramePrincipal framePrincipal = new FramePrincipal();
        ControlDeNavegacion controlDeNavegacion = new ControlDeNavegacion(framePrincipal);
        framePrincipal.setExtendedState(JFrame.MAXIMIZED_BOTH);
        framePrincipal.setVisible(true);

    }
}
