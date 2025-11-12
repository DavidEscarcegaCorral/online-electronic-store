package paneles;

import estilos.CustomRadioButton;
import estilos.Estilos;
import estilos.FontUtil;

import javax.swing.*;
import java.awt.*;

public class OpcionEntregaPanel extends JPanel {
    private JLabel tituloLbl;
    private JLabel rb1Label;
    private JLabel rb2Label;
    private JPanel panelTitulo;
    private JPanel panelOpciones;
    private CustomRadioButton rb1;
    private CustomRadioButton rb2;

    public OpcionEntregaPanel(){
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(220,350));

        tituloLbl = new JLabel("Opciones de entrega");
        tituloLbl.setForeground(Color.white);
        tituloLbl.setFont(FontUtil.loadFont(30, "Iceland-Regular"));

        // Panel Titulo
        panelTitulo = new JPanel();
        panelTitulo.setOpaque(false);
        panelTitulo.setPreferredSize(new Dimension(250, 45));

        // Panel de opciones
        panelOpciones = new JPanel();
        panelOpciones.setOpaque(false);
        panelOpciones.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 25));
        panelOpciones.setPreferredSize(new Dimension(220, 500));

        rb1 = new CustomRadioButton("Recoger en sucursal: Gratis");
        rb2 = new CustomRadioButton("Envio estandar: $224.00 mxn");

        panelTitulo.add(tituloLbl);
        panelOpciones.add(rb1);
        panelOpciones.add(rb2);

        add(panelTitulo);
        add(panelOpciones);


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
