package armadoPC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MarcaProcesadorCard extends JPanel {
    private JLabel imagenMarcaLbl;
    private String nombreMarca;
    private String imagenUrl;
    private java.util.function.Consumer<String> onMarcaSelected;

    public MarcaProcesadorCard(String nombreMarca, String imagenUrl) {
        this.nombreMarca = nombreMarca;
        this.imagenUrl = imagenUrl;

        initComponentes();
        añadirComponentes();
        setupListeners();
    }

    public void setOnMarcaSelected(java.util.function.Consumer<String> callback) {
        this.onMarcaSelected = callback;
    }

    private void initComponentes() {
        setOpaque(false);
        setPreferredSize(new Dimension(260, 220));
        setMaximumSize(new Dimension(260, 220));
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
            Image scaledImage = originalImage.getScaledInstance(220, -1, Image.SCALE_SMOOTH);
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
                System.out.println("Marca seleccionada: " + nombreMarca);
                if (onMarcaSelected != null) {
                    onMarcaSelected.accept(nombreMarca);
                }
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
