package armadoPC;

import compartido.estilos.FontUtil;
import compartido.estilos.Estilos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class CategoriaCard extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaCard.class);

    private JLabel imagenCategoriaLbl;
    private JLabel nombreCategoriaLbl;

    private String nombreCategoria;
    private String imagenUrl;
    private Consumer<String> onCategoriaSelected;
    private boolean seleccionado = false;

    public CategoriaCard(String nombreCategoria, String imagenUrl) {
        this.nombreCategoria = nombreCategoria;
        this.imagenUrl = imagenUrl;

        initComponents();
        añadirComponentes();
        setupListeners();
    }

    public void setOnCategoriaSelected(Consumer<String> callback) {
        this.onCategoriaSelected = callback;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
        repaint();
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
            ImageIcon icon = null;
            java.net.URL location = null;

            // Cargar directamente desde la ruta proporcionada (las categorías siempre tienen la misma ruta)
            if (path != null && !path.trim().isEmpty()) {
                location = getClass().getResource(path);
                if (location != null) {
                    icon = new ImageIcon(location);
                }
            }

            // Si no se encuentra, usar imagen por defecto
            if (icon == null || icon.getIconWidth() == -1) {
                logger.debug("Imagen de categoría no encontrada en: {}. Usando imagen por defecto.", path);
                location = getClass().getResource("/img/productos/default.png");
                if (location != null) {
                    icon = new ImageIcon(location);
                }
            }

            // Si tenemos un icono válido, escalarlo y mostrarlo
            if (icon != null && icon.getIconWidth() != -1) {
                ImageIcon scaledIcon = getScaledIcon(icon, targetWidth, targetHeight);
                imagenCategoriaLbl.setIcon(scaledIcon);
                imagenCategoriaLbl.setText("");
            } else {
                imagenCategoriaLbl.setIcon(null);
            }

        } catch (Exception e) {
            logger.error("Error al cargar la imagen para categoría: {}", nombreCategoria, e);
            imagenCategoriaLbl.setIcon(null);
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
        if (seleccionado) {
            g2d.setColor(Estilos.COLOR_ENFASIS);
            g2d.setStroke(new BasicStroke(4));
            g2d.drawRoundRect(2, 2, getWidth()-5, getHeight()-5, 25, 25);
        }
        g2d.dispose();
    }

}
