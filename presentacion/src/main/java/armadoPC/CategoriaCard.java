package armadoPC;

import compartido.estilos.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CategoriaCard extends JPanel {
    private JLabel imagenCategoriaLbl;
    private JLabel nombreCategoriaLbl;

    private String nombreCategoria;
    private String imagenUrl;
    // callback que notifica la seleccion de categoria
    private java.util.function.Consumer<String> onCategoriaSelected;

    public CategoriaCard(String nombreCategoria, String imagenUrl) {
        this.nombreCategoria = nombreCategoria;
        this.imagenUrl = imagenUrl;

        initComponents();
        añadirComponentes();
        setupListeners();

    }

    public void setOnCategoriaSelected(java.util.function.Consumer<String> callback) {
        this.onCategoriaSelected = callback;
    }

    private void initComponents() {
        setOpaque(false);
        setPreferredSize(new Dimension(180, 250));
        setMaximumSize(new Dimension(180, 250));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        imagenCategoriaLbl = new JLabel();
        imagenCategoriaLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        int maxImgAncho = 150;
        int maxImgAlto = 150;

        Dimension imgSize = new Dimension(maxImgAncho, maxImgAlto);
        imagenCategoriaLbl.setPreferredSize(imgSize);
        imagenCategoriaLbl.setMinimumSize(imgSize);
        imagenCategoriaLbl.setMaximumSize(imgSize);

        imagenCategoriaLbl.setHorizontalAlignment(SwingConstants.CENTER);
        imagenCategoriaLbl.setVerticalAlignment(SwingConstants.CENTER);

        cargarImagen(this.imagenUrl, maxImgAncho, maxImgAlto);

        // Label del nombre
        nombreCategoriaLbl = new JLabel(nombreCategoria);
        nombreCategoriaLbl.setFont(FontUtil.loadFont(25, "IBMPlexSans-Bold"));
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

    private void cargarImagen(String path, int targetWidth, int targetHeight) {
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(path));
            ImageIcon scaledIcon = getScaledIcon(originalIcon, targetWidth, targetHeight);
            imagenCategoriaLbl.setIcon(scaledIcon);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen para " + nombreCategoria + ": " + path);
            imagenCategoriaLbl.setText("No Image");
            imagenCategoriaLbl.setIcon(null);
            e.printStackTrace();
        }
    }

    private ImageIcon getScaledIcon(ImageIcon srcIcon, int targetWidth, int targetHeight) {

        int originalWidth = srcIcon.getIconWidth();
        int originalHeight = srcIcon.getIconHeight();

        if (originalWidth == 0 || originalHeight == 0) {
            return srcIcon;
        }
        if (originalWidth <= targetWidth && originalHeight <= targetHeight) {
            return srcIcon;
        }

        double widthRatio = (double) targetWidth / (double) originalWidth;
        double heightRatio = (double) targetHeight / (double) originalHeight;

        double ratio = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        Image scaledImage = srcIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        return new ImageIcon(scaledImage);
    }

    private void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Categoria clickeada: " + nombreCategoria);
                if (onCategoriaSelected != null) {
                    onCategoriaSelected.accept(nombreCategoria);
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
