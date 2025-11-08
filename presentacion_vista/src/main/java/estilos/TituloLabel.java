package estilos;

import javax.swing.*;
import java.awt.*;

public class TituloLabel extends JLabel {
    private String titulo;

    public TituloLabel(String titulo){
        setText(titulo);
        setFont(FontUtil.loadFont( 24, "Inter_Light"));
        setForeground(Color.white);
        setHorizontalAlignment(JLabel.CENTER);
        setOpaque(false);
        setPreferredSize(new Dimension(230, 40));
    }

    public void setTitulo(String titulo){
        setText(titulo);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Estilos.COLOR_TITULO_LABEL);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        super.paintComponent(g2d);
        g2d.dispose();
    }
}
