package presentacion.panels.carrito;

import estilos.Estilos;
import estilos.FontUtil;

import javax.swing.*;
import java.awt.*;

public class OpcionEntregaMenu extends JPanel {
    private JLabel tituloLbl;
    private JLabel rb1Label;
    private JLabel rb2Label;
    private JRadioButtonMenuItem rb1;
    private JRadioButtonMenuItem rb2;

    public OpcionEntregaMenu(){
        setOpaque(false);
        setPreferredSize(new Dimension(240, 610));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        tituloLbl = new JLabel("Opciones de entrega");
        tituloLbl.setForeground(Color.white);
        tituloLbl.setFont(FontUtil.loadFont(32, "Iceland-Regular"));

        rb1Label = new JLabel("Opcion 1");
        rb2Label = new JLabel("Opcion 2");

        rb1 = new JRadioButtonMenuItem();
        rb2 = new JRadioButtonMenuItem();

        add(tituloLbl);
//        add(rb1);
//        add(rb2);

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
