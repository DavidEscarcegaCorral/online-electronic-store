package presentacion.panels;

import estilos.Estilos;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelDestacados extends JPanel{
    private JPanel cuadrado1;
    private JPanel cuadrado2;
    private JPanel cuadrado3;
    private JLabel textoCuadrado1;
    private JLabel textoCuadrado2;
    private JLabel textoCuadrado3;
    
    public PanelDestacados(){
        setPreferredSize(new Dimension(1100, 450));
        setOpaque(false);
        setLayout(new GridBagLayout());
        Font font = new Font("Arial", Font.PLAIN, 12);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        cuadrado1 = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(99, 99, 99));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2d.dispose();
            }
        };
        cuadrado1.setOpaque(false);
        textoCuadrado1 = new JLabel("Nuevos productos", SwingConstants.CENTER);
        textoCuadrado1.setFont(font);
        textoCuadrado1.setForeground(Color.white);
        textoCuadrado1.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        cuadrado1.add(textoCuadrado1, BorderLayout.NORTH);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        add(cuadrado1, gbc);
        
        cuadrado2 = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(99, 99, 99));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2d.dispose();
            }
        };
        cuadrado2.setOpaque(false);
        textoCuadrado2 = new JLabel("Lo mas vendido", SwingConstants.CENTER);
        textoCuadrado2.setFont(font);
        textoCuadrado2.setForeground(Color.white);
        textoCuadrado2.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        cuadrado2.add(textoCuadrado2, BorderLayout.NORTH);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        add(cuadrado2, gbc);
        
        cuadrado3 = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(178, 91, 254));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2d.dispose();
            }
        };
        cuadrado3.setOpaque(false);
        textoCuadrado3 = new JLabel("Oferta especial", SwingConstants.CENTER);
        textoCuadrado3.setFont(font);
        textoCuadrado3.setForeground(Color.white);
        textoCuadrado3.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        cuadrado3.add(textoCuadrado3, BorderLayout.NORTH);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        add(cuadrado3, gbc);
    }
}
