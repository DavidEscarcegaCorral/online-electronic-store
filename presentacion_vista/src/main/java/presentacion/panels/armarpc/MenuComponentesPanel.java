package presentacion.panels.armarpc;

import estilos.Estilos;
import estilos.FontUtil;

import javax.swing.*;
import java.awt.*;

public class MenuComponentesPanel extends JPanel {
    private JLabel titulo;
    private JLabel procesador;
    private JLabel tarjetaMadre;
    private JLabel memoriaRAM;
    private JLabel almacenamiento;
    private JLabel unidadSSD;
    private JLabel tarjetaDeVideo;
    private JLabel fuenteDePoder;
    private JLabel disipador;
    private JLabel Ventilador;
    private JLabel Monitor;
    private JLabel kitTecladoRaton;
    private JLabel red;

    public MenuComponentesPanel() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titulo = new JLabel("Menú");
        titulo.setFont(FontUtil.loadFont(15, "Inter_Regular"));
        titulo.setForeground(Color.white);

        add(titulo);
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
