package presentacion.panels.armarpc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CategoriaPanel extends JPanel {
    private JLabel imagenCategoriaLbl;
    private JLabel nombreCategoriaLbl;

    private String nombreCategoria;
    private String imagenUrl;

    public CategoriaPanel(String nombreCategoria, String imagenUrl) {
        this.nombreCategoria = nombreCategoria;
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

        imagenCategoriaLbl = new JLabel();
        imagenCategoriaLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        cargarImagen(this.imagenUrl);

        // Label del nombre
        nombreCategoriaLbl = new JLabel("<html><div style='text-align: center;'>" + nombreCategoria + "</div></html>");
        nombreCategoriaLbl.setFont(new Font("Arial", Font.PLAIN, 18));
        nombreCategoriaLbl.setForeground(Color.BLACK);
        nombreCategoriaLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        nombreCategoriaLbl.setHorizontalAlignment(SwingConstants.CENTER);

    }

    private void añadirComponentes() {
        add(Box.createVerticalStrut(10));
        add(imagenCategoriaLbl);
        add(Box.createVerticalStrut(20));
        add(nombreCategoriaLbl);

    }

    private void cargarImagen(String path) {
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(path));
            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(100, -1, Image.SCALE_SMOOTH);
            imagenCategoriaLbl.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen para " + nombreCategoria + ": " + path);
            imagenCategoriaLbl.setText("No Image");
            imagenCategoriaLbl.setIcon(null);
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Tarjeta de producto clickeada: " + nombreCategoria);

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
        g2d.dispose();
    }

}
