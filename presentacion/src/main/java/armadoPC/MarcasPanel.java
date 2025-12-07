package armadoPC;

import compartido.estilos.Estilos;
import compartido.estilos.Boton;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class MarcasPanel extends JPanel {

    private Consumer<String> onMarcaSelected;
    private JPanel marcasContainer;

    public MarcasPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());

        marcasContainer = new JPanel();
        marcasContainer.setOpaque(false);
        marcasContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 25));

        add(marcasContainer, BorderLayout.CENTER);
    }

    public void setOnMarcaSelected(Consumer<String> callback) {
        this.onMarcaSelected = callback;
    }

    public void cargarMarcas(List<String> marcas) {
        marcasContainer.removeAll();

        if (marcas == null || marcas.isEmpty()) {
            JLabel sinMarcas = new JLabel("No hay marcas disponibles");
            sinMarcas.setForeground(Color.WHITE);
            sinMarcas.setFont(new Font("Arial", Font.BOLD, 18));
            marcasContainer.add(sinMarcas);
        } else {
            for (String marca : marcas) {
                Boton marcaBtn = new Boton(marca, 200, 100, 18, 15,
                    Color.white, Estilos.COLOR_NAV_INF, Estilos.COLOR_BACKGROUND);
                marcaBtn.setNewFont();

                marcaBtn.addActionListener(e -> {
                    if (onMarcaSelected != null) {
                        onMarcaSelected.accept(marca);
                    }
                });

                marcasContainer.add(marcaBtn);
            }
        }

        revalidate();
        repaint();
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

