package armadoPC;

import compartido.estilos.Estilos;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class MarcaProcesadorPanel extends JPanel {
    private static MarcaProcesadorCard intel = new MarcaProcesadorCard("Intel", "/img/marcas/intelLogo.png");
    private static MarcaProcesadorCard amd = new MarcaProcesadorCard("AMD", "/img/marcas/AMDLogo.png");
    private java.util.function.Consumer<String> onMarcaSelected;
    private String seleccionActual = null;

    public MarcaProcesadorPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 25, 25));
        add(intel);
        add(amd);

        configurarCallbacks();
    }

    private void configurarCallbacks() {
        intel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if ("Intel".equals(seleccionActual)) {
                    intel.setSeleccionado(false);
                    seleccionActual = null;
                    if (onMarcaSelected != null) onMarcaSelected.accept(null);
                } else {
                    intel.setSeleccionado(true);
                    amd.setSeleccionado(false);
                    seleccionActual = "Intel";
                    if (onMarcaSelected != null) onMarcaSelected.accept("Intel");
                }
            }
        });

        amd.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if ("AMD".equals(seleccionActual)) {
                    amd.setSeleccionado(false);
                    seleccionActual = null;
                    if (onMarcaSelected != null) onMarcaSelected.accept(null);
                } else {
                    amd.setSeleccionado(true);
                    intel.setSeleccionado(false);
                    seleccionActual = "AMD";
                    if (onMarcaSelected != null) onMarcaSelected.accept("AMD");
                }
            }
        });
    }

    public void setOnMarcaSelected(Consumer<String> callback) {
        this.onMarcaSelected = callback;
    }

    public String getSeleccionActual() {
        return seleccionActual;
    }

    public void limpiarSeleccion() {
        intel.setSeleccionado(false);
        amd.setSeleccionado(false);
        seleccionActual = null;
        repaint();
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
