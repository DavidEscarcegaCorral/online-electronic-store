package presentacion.panels.armarpc;

import estilos.Boton;
import estilos.Estilos;
import estilos.FontUtil;
import estilos.TituloLabel;
import presentacion.panels.PanelBase;

import javax.swing.*;
import java.awt.*;

public class ArmarPcPanel extends PanelBase {
    private static String titulo = "Armar PC";
    private CategoriasPanel categoriasPanel;
    private MarcaProcesadorPanel marcasPanel;

    private JLabel tituloLbl;
    private TituloLabel subTItuloLabel;

    private Boton continuarBtn;

    public ArmarPcPanel() {
        super();
        tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(FontUtil.loadFont(28, "Inter_SemiBold"));
        tituloLbl.setForeground(Color.white);
        subTItuloLabel = new TituloLabel("CATEGORIAS");
        subTItuloLabel.setForeground(Color.white);

        categoriasPanel = new CategoriasPanel();
        marcasPanel = new MarcaProcesadorPanel();

        // Boton
        continuarBtn = new Boton("Continuar", 130, 30, 16, 25, Color.white, Estilos.COLOR_ENFASIS, Estilos.COLOR_ENFASIS_HOOVER);

        // Panel Norte
        panelNorte.add(tituloLbl);
        panelNorte.add(subTItuloLabel);

        // Panel Centro
        panelCentro.add(categoriasPanel);

        // Panel Sur
        panelSur.add(continuarBtn);

    }

    public Boton getContinuarBtn() {
        return continuarBtn;
    }

//    public void setContenido(){
//        if (this.panelContenido != null) {
//            panelContenedor.remove(this.panelContenido);
//        }
//
//        this.panelContenido = panelContenido;
//        panelContenedor.add(this.panelContenido, BorderLayout.CENTER);
//        panelContenedor.revalidate();
//        panelContenedor.repaint();
//    }
}
