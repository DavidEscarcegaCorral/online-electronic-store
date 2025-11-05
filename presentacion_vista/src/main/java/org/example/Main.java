package org.example;

import controlPresentacionVista.ControlDeNavegacion;
import presentacion.frames.FramePrincipal;

public class Main {
    public static void main(String[] args) {
        FramePrincipal framePrincipal = new  FramePrincipal();
        ControlDeNavegacion controlDeNavegacion  = new ControlDeNavegacion(framePrincipal);
        framePrincipal.setVisible(true);
    }
}