package presentacion.panels.cards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MarcaProcesadorCard extends JPanel {
    private JLabel imagenMarcaLbl;

    private String nombreMarca;
    private String imagenUrl;

    public MarcaProcesadorCard(String nombreMarca, String imagenUrl) {
        this.nombreMarca = nombreMarca;
        this.imagenUrl = imagenUrl;

        initComponents();
        añadirComponentes();
        setupListeners();

    }

    private void initComponents() {
        setOpaque(false);
        setPreferredSize(new Dimension(120, 170));
        setMaximumSize(new Dimension(120, 170));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        imagenMarcaLbl = new JLabel();
        imagenMarcaLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        cargarImagen(this.imagenUrl);

    }

    private void añadirComponentes() {
        add(Box.createVerticalStrut(10));
        add(imagenMarcaLbl);
        add(Box.createVerticalStrut(20));
        add(imagenMarcaLbl);

    }

    private void cargarImagen(String path) {
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(path));
            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(100, -1, Image.SCALE_SMOOTH);
            imagenMarcaLbl.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen para " + nombreMarca + ": " + path);
            imagenMarcaLbl.setText("No Image");
            imagenMarcaLbl.setIcon(null);
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Tarjeta de producto clickeada: " + nombreMarca);
            }
        });
    }


}
