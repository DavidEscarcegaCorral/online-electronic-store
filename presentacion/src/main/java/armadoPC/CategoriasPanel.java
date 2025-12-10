package armadoPC;


import compartido.estilos.Estilos;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CategoriasPanel extends JPanel {
    private static CategoriaCard categoriaCardGamer = new CategoriaCard("GAMER", "/img/categorias/gamer.png");
    private static CategoriaCard categoriaCardOffice = new CategoriaCard("OFFICE", "/img/categorias/oficina.png");
    private static CategoriaCard categoriaCardDesing = new CategoriaCard("DESING", "/img/categorias/desing.png");
    private static CategoriaCard categoriaCardCustom = new CategoriaCard("CUSTOM", "/img/categorias/custom.png");

    private Consumer<String> onCategoriaSeleccionada;
    private String seleccionActual = null;

    public String getSeleccionActual() {
        return seleccionActual;
    }

    public void limpiarSeleccion() {
        categoriaCardGamer.setSeleccionado(false);
        categoriaCardOffice.setSeleccionado(false);
        categoriaCardDesing.setSeleccionado(false);
        categoriaCardCustom.setSeleccionado(false);
        seleccionActual = null;
        repaint();
    }

    public void setOnCategoriaSeleccionada(Consumer<String> callback) {
        this.onCategoriaSeleccionada = callback;
        categoriaCardGamer.setOnCategoriaSelected(this::categoriaClick);
        categoriaCardOffice.setOnCategoriaSelected(this::categoriaClick);
        categoriaCardDesing.setOnCategoriaSelected(this::categoriaClick);
        categoriaCardCustom.setOnCategoriaSelected(this::categoriaClick);
    }

    private void categoriaClick(String nombre) {
        if (nombre != null && nombre.equals(seleccionActual)) {
            categoriaCardGamer.setSeleccionado(false);
            categoriaCardOffice.setSeleccionado(false);
            categoriaCardDesing.setSeleccionado(false);
            categoriaCardCustom.setSeleccionado(false);
            seleccionActual = null;
            if (onCategoriaSeleccionada != null) onCategoriaSeleccionada.accept(null);
            return;
        }

        categoriaCardGamer.setSeleccionado(false);
        categoriaCardOffice.setSeleccionado(false);
        categoriaCardDesing.setSeleccionado(false);
        categoriaCardCustom.setSeleccionado(false);

        switch (nombre) {
            case "GAMER": categoriaCardGamer.setSeleccionado(true); break;
            case "OFFICE": categoriaCardOffice.setSeleccionado(true); break;
            case "DESING": categoriaCardDesing.setSeleccionado(true); break;
            case "CUSTOM": categoriaCardCustom.setSeleccionado(true); break;
        }

        seleccionActual = nombre;
        if (onCategoriaSeleccionada != null) onCategoriaSeleccionada.accept(nombre);
    }

    public CategoriasPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 25, 25));
        add(categoriaCardGamer);
        add(categoriaCardOffice);
        add(categoriaCardDesing);
        add(categoriaCardCustom);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Estilos.COLOR_NAV_INF);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        g2d.dispose();
    }

}
